package com.example.baohuynh.mymovieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickMovieItem;
import com.example.baohuynh.mymovieapp.handler.CallbackGenres;
import com.example.baohuynh.mymovieapp.handler.GetGenresJson;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

import static com.example.baohuynh.mymovieapp.fragment.MovieFragment.MOVIE_LIST;
import static com.example.baohuynh.mymovieapp.fragment.MovieFragment.POSITION;

public class Genres extends AppCompatActivity implements CallbackGenres, OnLoadMoreListener,
        CallBackOnClickMovieItem {
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ArrayList<Movie> mTemps;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        initToolbar();
        getGenres(mPage);
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
    public void onGetGenresSuccess(ArrayList<Movie> movies) {
        if (mTemps == null) {
            mTemps = movies;
            mRecyclerView = findViewById(R.id.recycler_genres);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, MovieFragment.SPAN_COUNT);
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
    public void onGetGenresFail(Throwable e) {

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
        mPage++;
        getGenres(mPage);
        mMovieAdapter.setLoading(true);
    }

    @Override
    public void onClick(int position) {
        Intent iMovieGenres = new Intent(this, MovieDetail.class);
        iMovieGenres.putParcelableArrayListExtra(MOVIE_LIST, mMovies);
        iMovieGenres.putExtra(POSITION, position);
        startActivity(iMovieGenres);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getGenres(int page) {
        int genres_id = getIntent().getIntExtra(MainActivity.GENRES_KEY, 1);
        GetGenresJson genresJson = new GetGenresJson(mMovies, this);
        switch (genres_id) {
            case MainActivity.ACTION_ID:
                setTitle(R.string.action);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.ACTION_ID, page));
                break;
            case MainActivity.ADVENTURE_ID:
                setTitle(R.string.adventure);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.ADVENTURE_ID, page));
                break;
            case MainActivity.ANIMATION_ID:
                setTitle(R.string.animation);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.ANIMATION_ID, page));
                break;
            case MainActivity.COMEDY_ID:
                setTitle(R.string.comedy);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.COMEDY_ID, page));
                break;
            case MainActivity.CRIME_ID:
                setTitle(R.string.crime);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.CRIME_ID, page));
                break;
            case MainActivity.DRAMA_ID:
                setTitle(R.string.drama);
                genresJson.execute(MovieAPI.getMovieGenres(MainActivity.DRAMA_ID, page));
                break;
        }
    }
}
