package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

/**
 * Created by baohuynh on 05/04/2018.
 */

public interface CallbackGetSimilarMovie {
    void onGetSimilarMovieSuccess(ArrayList<Movie> movies);
    void onGetSimilarMovieFail(Throwable e);
}
