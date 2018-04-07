package com.example.baohuynh.mymovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickMovieItem;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by baohuynh on 20/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOAD = 0;
    //Load data is false
    private boolean isLoading = false;
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private CallBackOnClickMovieItem mClickMovieItem;
    private OnLoadMoreListener mLoadMoreListener;

    public MovieAdapter(Context context, ArrayList<Movie> movies, RecyclerView recyclerView) {
        mContext = context;
        mMovies = movies;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleThreshold = 5;
                int totalItemCount = manager.getItemCount();
                int lastVisibleItem = manager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    mLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.display_movie, parent, false);
            return new MovieHolder(view, mClickMovieItem);
        } else if (viewType == VIEW_TYPE_LOAD) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loadmore_recyclerview, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieHolder) {
            MovieHolder movieHolder = (MovieHolder) holder;
            movieHolder.addData(mContext, mMovies.get(position));
        } else if (holder instanceof LoadingHolder) {
            LoadingHolder loadingHolder = (LoadingHolder) holder;
            loadingHolder.mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMovies.get(position) == null ? VIEW_TYPE_LOAD : VIEW_TYPE_ITEM;
    }

    public void setClickMovieItem(CallBackOnClickMovieItem clickMovieItem) {
        mClickMovieItem = clickMovieItem;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setLoading(boolean loading) {
        isLoading =  loading;
    }


    private static class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView imgMovie;
        private TextView txtMovieName;
        private CallBackOnClickMovieItem mClickMovieItem;

        private MovieHolder(final View itemView, final CallBackOnClickMovieItem item) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.img_movie);
            txtMovieName = itemView.findViewById(R.id.tv_movie_name);
            mClickMovieItem = item;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickMovieItem.onClick(getAdapterPosition());
                }
            });
        }

        private void addData(Context context, Movie movie) {
            Picasso.with(context).load(movie.getImgMovie()).into(imgMovie);
            txtMovieName.setText(movie.getTxtMovieName());
        }
    }

    private static class LoadingHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        private LoadingHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progress_loadmore);
        }
    }
}
