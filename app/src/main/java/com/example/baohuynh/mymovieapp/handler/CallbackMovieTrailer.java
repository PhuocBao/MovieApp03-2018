package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Trailer;
import java.util.ArrayList;

/**
 * Created by baohuynh on 30/03/2018.
 */

public interface CallbackMovieTrailer {
    void getTrailerSuccess(ArrayList<Trailer> trailers);
    void getTrailerFail(Throwable e);
}
