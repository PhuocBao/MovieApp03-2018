package com.example.baohuynh.mymovieapp.model;

/**
 * Created by baohuynh on 30/03/2018.
 */

public class Trailer {
    private String idTrailer, keyTrailer;

    public Trailer(String idTrailer, String keyTrailer) {
        this.idTrailer = idTrailer;
        this.keyTrailer = keyTrailer;
    }

    public String getIdTrailer() {
        return idTrailer;
    }

    public void setIdTrailer(String idTrailer) {
        this.idTrailer = idTrailer;
    }

    public String getKeyTrailer() {
        return keyTrailer;
    }

    public void setKeyTrailer(String keyTrailer) {
        this.keyTrailer = keyTrailer;
    }
}
