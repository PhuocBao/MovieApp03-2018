package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

/**
 * Created by baohuynh on 07/04/2018.
 */

public interface CallbackGenres {
    void onGetGenresSuccess(ArrayList<Movie> movies);
    void onGetGenresFail(Throwable e);
}
