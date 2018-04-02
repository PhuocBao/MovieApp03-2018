package com.example.baohuynh.mymovieapp;

/**
 * Created by baohuynh on 20/03/2018.
 */

public final class MovieAPI {
    public static final String GET_IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String YOUTUBE_API = "AIzaSyBXl61UD99Ls-3o4ImdL9TfQtnXObnj51U";
    private static final String MOVIE_KEY = "8ec3fbf1c1b06d940e29c592421917ae";
    private static final String SITE = "https://api.themoviedb.org/3/movie/";
    private static final String LANGUAGE = "&language=en-US";
    private static final String PAGE = "&page=";
    private static final String POPULAR = "popular?api_key=";
    private static final String NOW_PLAYING = "now_playing?api_key=";
    private static final String UPCOMING = "upcoming?api_key=";
    private static final String TOP_RATE = "top_rated?api_key=";

    public MovieAPI() {
    }

    public static String getPopular(int page) {
        return SITE + POPULAR + MOVIE_KEY + LANGUAGE + PAGE + page;
    }

    public static String getNowPlaying(int page) {
        return SITE + NOW_PLAYING + MOVIE_KEY + LANGUAGE + PAGE + page;
    }

    public static String getUpcoming(int page) {
        return SITE + UPCOMING + MOVIE_KEY + LANGUAGE + PAGE + page;
    }

    public static String getTopRate(int page) {
        return SITE + TOP_RATE + MOVIE_KEY + LANGUAGE + PAGE + page;
    }

    public static String getActorMovie(int idMovie) {
        return SITE + idMovie + "/credits" + "?api_key=" + MOVIE_KEY;
    }

    public static String getTrailerMovie(int idMovie) {
        return SITE + idMovie + "/videos" + "?api_key=" + MOVIE_KEY;
    }
}
