package com.example.baohuynh.mymovieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickMovieItem;
import com.example.baohuynh.mymovieapp.handler.CallbackGetSimilarMovie;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieTrailer;
import com.example.baohuynh.mymovieapp.handler.GetMovieTrailerJson;
import com.example.baohuynh.mymovieapp.handler.GetSimilarMovieJson;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.example.baohuynh.mymovieapp.model.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.util.ArrayList;

public class PlayTrailer extends YouTubeBaseActivity
        implements CallbackMovieTrailer, CallbackGetSimilarMovie, OnLoadMoreListener,
        CallBackOnClickMovieItem {
    private ArrayList<Trailer> mTrailers = new ArrayList<>();
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private ArrayList<Movie> mTempList;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private int idMovie;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);
        idMovie = getIntent().getIntExtra(MovieDetail.MOVIE_ID, 1);
        GetMovieTrailerJson getMovieTrailerJson = new GetMovieTrailerJson(mTrailers, this);
        getMovieTrailerJson.execute(MovieAPI.getTrailerMovie(idMovie));
        getSimilarMovie(mPage);
    }

    @Override
    public void getTrailerSuccess(final ArrayList<Trailer> trailers) {
        YouTubePlayerView playerView = findViewById(R.id.youtube_player);
        YouTubePlayer.OnInitializedListener onInitializedListener =
                new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        ArrayList<String> keys = new ArrayList<>();
                        for (int i = 0; i < trailers.size(); i++) {
                            keys.add(trailers.get(i).getKeyTrailer());
                        }
                        youTubePlayer.loadVideos(keys);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                            YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(PlayTrailer.this,
                                "You need to run version 4.2.16 of the mobile YouTube app (or "
                                        + "higher) to use the API.", Toast.LENGTH_LONG).show();
                    }
                };
        playerView.initialize(MovieAPI.YOUTUBE_API, onInitializedListener);
    }

    @Override
    public void getTrailerFail(Throwable e) {
        //TODO: do it later
    }

    @Override
    public void onGetSimilarMovieSuccess(final ArrayList<Movie> movies) {
        mRecyclerView = findViewById(R.id.recycler_similar_movie);
        if (mTempList == null) {
            mTempList = movies;
            GridLayoutManager gridLayoutManager =
                    new GridLayoutManager(this, MovieFragment.SPAN_COUNT);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mMovieAdapter = new MovieAdapter(this, mTempList, mRecyclerView);
            mRecyclerView.setAdapter(mMovieAdapter);
            mMovieAdapter.setClickMovieItem(this);
            mMovieAdapter.setLoadMoreListener(this);
        } else {
            //if arraylist not null, insert item into progress loading position = size - 1
            //the list will not reload and back to top of view
            mMovieAdapter.notifyItemInserted(mTempList.size() - 1);
            mMovieAdapter.setLoading(false);
        }
    }

    @Override
    public void onGetSimilarMovieFail(Throwable e) {
        //TODO: DO LATER
    }

    @Override
    public void onLoadMore() {
        mMovies.add(null);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                //when progress is loading, remove progress position when it loaded
                //if not, the progress always in loading status
                int positionLoading = mMovies.size() - 1;
                mMovies.remove(positionLoading);
                mMovieAdapter.notifyItemRemoved(positionLoading);
            }
        });
        mPage++;
        getSimilarMovie(mPage);
        mMovieAdapter.setLoading(true);
    }

    @Override
    public void onClick(int position) {
        Intent iDetailMovie = new Intent(this, MovieDetail.class);
        iDetailMovie.putParcelableArrayListExtra(MovieFragment.MOVIE_LIST, mMovies);
        iDetailMovie.putExtra(MovieFragment.POSITION, position);
        startActivity(iDetailMovie);
    }

    private void getSimilarMovie(int page) {
        GetSimilarMovieJson getSimilarMovieJson = new GetSimilarMovieJson(mMovies, this);
        getSimilarMovieJson.execute(MovieAPI.getSimilarMovie(idMovie, page));
    }
}
