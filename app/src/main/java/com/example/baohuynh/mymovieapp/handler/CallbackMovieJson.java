package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

/**
 * Created by baohuynh on 20/03/2018.
 */

public interface CallbackMovieJson {
    void CallbackSuccess(ArrayList<Movie> movies);
    void CallbackFail(Throwable e);
}
