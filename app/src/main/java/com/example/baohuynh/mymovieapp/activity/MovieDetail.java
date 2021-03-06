package com.example.baohuynh.mymovieapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.ActorAdapter;
import com.example.baohuynh.mymovieapp.data.DatabaseHelper;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickItem;
import com.example.baohuynh.mymovieapp.handler.CallbackActor;
import com.example.baohuynh.mymovieapp.handler.GetMovieActorJson;
import com.example.baohuynh.mymovieapp.model.Actor;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity implements CallbackActor, View.OnClickListener,
        CallBackOnClickItem {
    public static final String MOVIE_ID = "movie_id";
    public static final String NAME = "name";
    public static final String ACTOR_ID = "actor_id";
    public static final String ACTOR_IMG = "actor_img";
    private int mPosition;
    private boolean selectedItem;
    private ImageView mImageDetail;
    private TextView mTextNameDetail, mTextViewRelease, mTextViewRating, mTextViewOverview;
    private Button mButtonWatch;
    private ArrayList<Movie> mMovies;
    private ArrayList<Actor> mActors = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovies = getIntent().getParcelableArrayListExtra(MovieFragment.MOVIE_LIST);
        mPosition = getIntent().getIntExtra(MovieFragment.POSITION, 1);
        initView();
        initToolbar();
        getView();
        GetMovieActorJson getMovieActorJson = new GetMovieActorJson(mActors, this);
        getMovieActorJson.execute(MovieAPI.getActorMovie(mMovies.get(mPosition).getMovieID()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMovies = null;
                finish();
                break;
            case R.id.menu_fav:
                if (mDatabaseHelper.checkAlready(mMovies.get(mPosition).getMovieID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This movie has already added");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Remove it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDatabaseHelper.deleteData(mMovies.get(mPosition));
                            Toast.makeText(MovieDetail.this, "Removed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    //selectedItem = false
                    if (selectedItem) {
                        if (mDatabaseHelper != null) {
                            mDatabaseHelper.deleteData(mMovies.get(mPosition));
                            Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        setSelectedItem(false);
                    } else {
                        mDatabaseHelper.insertData(mMovies.get(mPosition));
                        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
                        setSelectedItem(true);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetSuccess(ArrayList<Actor> actors) {
        RecyclerView mRecyclerActor = findViewById(R.id.recycler_actor);
        mRecyclerActor.setAdapter(new ActorAdapter(this, actors, this));
        mRecyclerActor.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerActor.setHasFixedSize(true);
    }

    @Override
    public void onGetFail(Throwable e) {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Intent iPlayerTrailer = new Intent(this, PlayTrailer.class);
        iPlayerTrailer.putExtra(MOVIE_ID, mMovies.get(mPosition).getMovieID());
        startActivity(iPlayerTrailer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    private void getView() {
        Movie movie = mMovies.get(mPosition);
        Picasso.with(this).load(movie.getImgMovie()).into(mImageDetail);
        mTextNameDetail.setText(movie.getTxtMovieName());
        mTextViewRelease.setText(movie.getReleaseDate());
        mTextViewRating.setText(String.format("%s", movie.getVoteAverage()));
        mTextViewOverview.setText(movie.getOverview());
        mButtonWatch.setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.detail);
    }

    private void initView() {
        mImageDetail = findViewById(R.id.img_detail);
        mTextNameDetail = findViewById(R.id.tv_name_detail);
        mTextViewRelease = findViewById(R.id.tv_release_detail);
        mTextViewRating = findViewById(R.id.tv_rate_detail);
        mTextViewOverview = findViewById(R.id.tv_movie_overview);
        mButtonWatch = findViewById(R.id.btn_watch_detail);
    }

    private void setSelectedItem(boolean isSelected) {
        selectedItem = isSelected;
    }

    @Override
    public void onClick(int position) {
        Intent iActorMovieList = new Intent(this, ActorMovieList.class);
        iActorMovieList.putExtra(MovieFragment.POSITION, position);
        iActorMovieList.putExtra(ACTOR_ID, mActors.get(position).getActorID());
        iActorMovieList.putExtra(NAME, mActors.get(position).getNameActor());
        iActorMovieList.putExtra(ACTOR_IMG, mActors.get(position).getImgActor());
        startActivity(iActorMovieList);
    }
}
