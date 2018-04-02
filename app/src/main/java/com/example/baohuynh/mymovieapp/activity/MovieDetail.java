package com.example.baohuynh.mymovieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.ActorAdapter;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallbackActor;
import com.example.baohuynh.mymovieapp.handler.GetMovieActorJson;
import com.example.baohuynh.mymovieapp.model.Actor;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity implements CallbackActor, View.OnClickListener {
    public static final String MOVIE_ID = "movie_id";
    private int mPosition;
    private ImageView mImageDetail;
    private TextView mTextNameDetail, mTextViewRelease, mTextViewRating, mTextViewOverview;
    private Button mButtonWatch;
    private ArrayList<Movie> mMovies;
    private ArrayList<Actor> mActors = new ArrayList<>();

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetSuccess(ArrayList<Actor> actors) {
        RecyclerView mRecyclerActor = findViewById(R.id.recycler_actor);
        mRecyclerActor.setAdapter(new ActorAdapter(this, actors));
        mRecyclerActor.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerActor.setHasFixedSize(true);
    }

    @Override
    public void onGetFail(Throwable e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Intent iPlayerTrailer = new Intent(this, PlayTrailer.class);
        iPlayerTrailer.putExtra(MOVIE_ID, mMovies.get(mPosition).getMovieID());
        startActivity(iPlayerTrailer);
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
}
