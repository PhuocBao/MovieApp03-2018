package com.example.baohuynh.mymovieapp.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.baohuynh.mymovieapp.activity.MovieDetail;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickMovieItem;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

public class FavoriteTab implements OnLoadMoreListener, CallBackOnClickMovieItem {
    private ArrayList<Movie> mMovies;
    private RecyclerView mRecyclerView;
    private Context mContext;

    public FavoriteTab(Context context, ArrayList<Movie> movies, RecyclerView recyclerView) {
        mContext = context;
        mMovies = movies;
        mRecyclerView = recyclerView;
    }

    public void setListFav() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        mMovies = databaseHelper.getAllMovie();
        MovieAdapter mMovieAdapter = new MovieAdapter(mContext, mMovies, mRecyclerView);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext, MovieFragment.SPAN_COUNT);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter.setLoadMoreListener(this);
        mMovieAdapter.setClickMovieItem(this);
    }

    @Override
    public void onLoadMore() {
        //TODO: do nothing
    }

    @Override
    public void onClick(int position) {
        Intent iFav = new Intent(mContext, MovieDetail.class);
        iFav.putExtra(MovieFragment.MOVIE_LIST, mMovies);
        iFav.putExtra(MovieFragment.POSITION, position);
        mContext.startActivity(iFav);
    }
}
