package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.Actor;
import java.util.ArrayList;

/**
 * Created by baohuynh on 29/03/2018.
 */

public interface CallbackActor {
    void onGetSuccess(ArrayList<Actor> mActor);
    void onGetFail(Throwable e);
}
