package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

public interface CallbackActorMovie {
    void onGetMASuccess(ArrayList<Movie> mMovie);
    void onGetMAFail(Throwable e);
}
