package com.example.baohuynh.mymovieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickItem;
import com.example.baohuynh.mymovieapp.handler.CallbackActorDetail;
import com.example.baohuynh.mymovieapp.handler.CallbackActorMovie;
import com.example.baohuynh.mymovieapp.handler.GetActorDetail;
import com.example.baohuynh.mymovieapp.handler.GetActorMovie;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.ActorDetail;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import static com.example.baohuynh.mymovieapp.fragment.MovieFragment.MOVIE_LIST;
import static com.example.baohuynh.mymovieapp.fragment.MovieFragment.POSITION;

public class ActorMovieList extends AppCompatActivity
        implements CallbackActorMovie, OnLoadMoreListener, CallBackOnClickItem,
        CallbackActorDetail {
    private RecyclerView mRecyclerView;
    private ImageView imgActor;
    private TextView txtName, txtBirth, txtDeath, txtBio, txtPlace;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private ArrayList<Movie> mTemps;
    private MovieAdapter mMovieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actor_detail);
        initView();
        int actor_id = getIntent().getIntExtra(MovieDetail.ACTOR_ID, 1);
        GetActorMovie getActorMovie = new GetActorMovie(mMovies, this);
        getActorMovie.execute(MovieAPI.getActor(actor_id, 1));
        setTitle(getIntent().getStringExtra(MovieDetail.NAME));
        Picasso.with(this).load(getIntent().getStringExtra(MovieDetail.ACTOR_IMG)).into(imgActor);
        //Log.d("link", "onCreate: "+MovieAPI.getActorDetail(actor_id));
        GetActorDetail getActorDetail = new GetActorDetail(this);
        getActorDetail.execute(MovieAPI.getActorDetail(actor_id));
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgActor = findViewById(R.id.img_actor_detail);
        txtBio = findViewById(R.id.txt_bio_actor);
        txtBirth = findViewById(R.id.txt_birth_actor);
        txtDeath = findViewById(R.id.txt_death_actor);
        txtName = findViewById(R.id.txt_fullname_actor);
        txtPlace = findViewById(R.id.txt_place_actor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                mMovies = null;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMASuccess(ArrayList<Movie> mMovie) {
        if (mTemps == null) {
            mTemps = mMovie;
            mRecyclerView = findViewById(R.id.recycler_actor_detail);
            GridLayoutManager gridLayoutManager =
                    new GridLayoutManager(this, MovieFragment.SPAN_COUNT);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mMovieAdapter = new MovieAdapter(this, mTemps, mRecyclerView);
            mRecyclerView.setAdapter(mMovieAdapter);
            mMovieAdapter.setLoadMoreListener(this);
            mMovieAdapter.setClickMovieItem(this);
        } else {
            mMovieAdapter.notifyItemInserted(mTemps.size() - 1);
            mMovieAdapter.setLoading(false);
        }
    }

    @Override
    public void onGetMAFail(Throwable e) {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        mMovies.add(null);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mMovies.remove(mMovies.size() - 1);
                mMovieAdapter.notifyItemRemoved(mMovies.size() - 1);
            }
        });
        //there is only 1 page to load all the movie
        //        mPage++;
        //        Log.d("page", "onLoadMore: "+mPage);
        //        updatePage(mPage);
        mMovieAdapter.setLoading(true);
    }

    @Override
    public void onClick(int position) {
        Intent iMovieActorList = new Intent(this, MovieDetail.class);
        iMovieActorList.putParcelableArrayListExtra(MOVIE_LIST, mMovies);
        iMovieActorList.putExtra(POSITION, position);
        startActivity(iMovieActorList);
    }

    @Override
    public void onGetActorDetailSuccess(ActorDetail actorDetails) {
        Log.d("name", "onGetActorDetailSuccess: "+actorDetails.getFullname());
        txtName.setText(String.format("Name: %s", actorDetails.getFullname()));
        txtPlace.setText(String.format("Place of birth: %s", actorDetails.getPlace()));
        txtBirth.setText(String.format("Birthday: %s", actorDetails.getBirth()));
        txtDeath.setText(String.format("Deathday: %s", actorDetails.getDeath()));
        txtBio.setText(String.format("Biography: %s", actorDetails.getBio()));
    }

    @Override
    public void onGetActorDetailFail(Throwable e) {
        Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
