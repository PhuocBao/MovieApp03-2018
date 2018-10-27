package com.example.baohuynh.mymovieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.activity.MovieDetail;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter;
import com.example.baohuynh.mymovieapp.data.FavoriteTab;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickItem;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieJson;
import com.example.baohuynh.mymovieapp.handler.GetMovieJson;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment
        implements CallbackMovieJson, CallBackOnClickItem, OnLoadMoreListener,
        SearchView.OnQueryTextListener {
    public static final String MOVIE_LIST = "movie_list";
    public static final String POSITION = "position";
    public static final int SPAN_COUNT = 3;
    private RecyclerView mRecyclerView;
    private int mPage = 1;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private ArrayList<Movie> mTempArr;
    private MovieAdapter movieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_main);
        updatePage(mPage);
        //can't init menu if you're not call this line
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onSuccess(ArrayList<Movie> movies) {
        if (mTempArr == null) {
            mTempArr = movies;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            movieAdapter = new MovieAdapter(getContext(), mTempArr, mRecyclerView);
            mRecyclerView.setAdapter(movieAdapter);
            movieAdapter.setLoadMoreListener(this);
            movieAdapter.setClickMovieItem(this);
        } else {
            //if arraylist not null, insert item into progress loading position = size - 1
            //the list will not reload and back to top of view
            movieAdapter.notifyItemInserted(mTempArr.size() - 1);
            movieAdapter.setLoading(false);
        }
    }

    @Override
    public void onFail(Throwable e) {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(int position) {
        Intent iMovieData = new Intent(getContext(), MovieDetail.class);
        iMovieData.putParcelableArrayListExtra(MOVIE_LIST, mMovies);
        iMovieData.putExtra(POSITION, position);
        startActivity(iMovieData);
    }

    public void updatePage(int page) {
        int position = getArguments().getInt(ViewPagerAdapter.FRAGMENT_KEY);
        GetMovieJson getMovie = new GetMovieJson(mMovies, this);
        switch (position) {
            case ViewPagerAdapter.TabItem.POPULAR:
                getMovie.execute(MovieAPI.getPopular(page));
                break;
            case ViewPagerAdapter.TabItem.NOW_PLAYING:
                getMovie.execute(MovieAPI.getNowPlaying(page));
                break;
            case ViewPagerAdapter.TabItem.UPCOMING:
                getMovie.execute(MovieAPI.getUpcoming(page));
                break;
            case ViewPagerAdapter.TabItem.TOP_RATE:
                getMovie.execute(MovieAPI.getTopRate(page));
                break;
            case ViewPagerAdapter.TabItem.FAVORITE:
                FavoriteTab favoriteTab = new FavoriteTab(getContext(), mTempArr, mRecyclerView);
                favoriteTab.setListFav();
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
                //when progress is loading, remove progress position when it loaded
                //if not, the progress always in loading status
                int positionItemProgress = mTempArr.size() - 1;
                mTempArr.remove(positionItemProgress);
                movieAdapter.notifyItemRemoved(positionItemProgress);
            }
        });
        mPage++;
        updatePage(mPage);
        movieAdapter.setLoading(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Movie> filterList = new ArrayList<>();
        for (Movie m : mMovies) {
            String movieName = m.getTxtMovieName().toLowerCase();
            if (movieName.contains(newText.toLowerCase())) {
                filterList.add(m);
            }
        }
        movieAdapter.setFilter(filterList);
        return true;
    }
}
