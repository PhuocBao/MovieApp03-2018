package com.example.baohuynh.mymovieapp.handler;

import com.example.baohuynh.mymovieapp.model.ActorDetail;
import java.util.ArrayList;

public interface CallbackActorDetail {
    void onGetActorDetailSuccess(ActorDetail actorDetails);
    void onGetActorDetailFail(Throwable e);
}
