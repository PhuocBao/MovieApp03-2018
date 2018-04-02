package com.example.baohuynh.mymovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.activity.MovieDetail;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickMovieItem;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieJson;
import com.example.baohuynh.mymovieapp.handler.GetMovieJson;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements CallbackMovieJson, CallBackOnClickMovieItem,
        OnLoadMoreListener {
    public static final String MOVIE_LIST = "movie_list";
    public static final String POSITION = "position";
    private static final int SPAN_COUNT = 3;
    private RecyclerView mRecyclerView;
    private int mPage = 1;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private MovieAdapter movieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_main);
        int defaultPage = 1;
        updatePage(defaultPage);
        return view;
    }

    @Override
    public void CallbackSuccess(ArrayList<Movie> movies) {
        movieAdapter = new MovieAdapter(getContext(), movies, mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(movieAdapter);
        mRecyclerView.setHasFixedSize(true);
        movieAdapter.setClickMovieItem(this);
        movieAdapter.setLoadMoreListener(this);
        movieAdapter.setLoading(false);
    }

    @Override
    public void CallbackFail(Throwable e) {
        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int position) {
        Intent iMovieData = new Intent(getContext(), MovieDetail.class);
        iMovieData.putParcelableArrayListExtra(MOVIE_LIST, mMovies);
        iMovieData.putExtra(POSITION, position);
        startActivity(iMovieData);
    }

    public void updatePage(int mPage) {
        int position = getArguments().getInt(ViewPagerAdapter.FRAGMENT_KEY);
        GetMovieJson getMovie = new GetMovieJson(mMovies, this);
        switch (position) {
            case ViewPagerAdapter.TabItem.POPULAR:
                getMovie.execute(MovieAPI.getPopular(mPage));
                break;
            case ViewPagerAdapter.TabItem.NOW_PLAYING:
                getMovie.execute(MovieAPI.getNowPlaying(mPage));
                break;
            case ViewPagerAdapter.TabItem.UPCOMING:
                getMovie.execute(MovieAPI.getUpcoming(mPage));
                break;
            case ViewPagerAdapter.TabItem.TOP_RATE:
                getMovie.execute(MovieAPI.getTopRate(mPage));
                break;
        }
    }

    @Override
    public void onLoadMore() {
        //add null , so the adapter will check view_type
        //and show progress bar at bottom
        mMovies.add(null);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                movieAdapter.notifyItemInserted(mMovies.size() - 1);
            }
        });
        mPage++;
        updatePage(mPage);
        movieAdapter.setLoading(true);
    }
}
