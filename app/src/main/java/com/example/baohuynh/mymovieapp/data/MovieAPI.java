package com.example.baohuynh.mymovieapp.data;

/**
 * Created by baohuynh on 20/03/2018.
 */

public final class MovieAPI {
    public static final String GET_IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String YOUTUBE_API = "AIzaSyBXl61UD99Ls-3o4ImdL9TfQtnXObnj51U";
    private static final String MOVIE_KEY = "8ec3fbf1c1b06d940e29c592421917ae";
    private static final String SITE = "https://api.themoviedb.org/3/movie/";
    private static final String SITE_GENRES = "https://api.themoviedb.org/3/genre/";
    private static final String SITE_ACTOR = "https://api.themoviedb.org/3/person/";
    private static final String LANGUAGE = "&language=en-US";
    private static final String PAGE = "&page=";
    private static final String POPULAR = "popular?api_key=";
    private static final String NOW_PLAYING = "now_playing?api_key=";
    private static final String UPCOMING = "upcoming?api_key=";
    private static final String TOP_RATE = "top_rated?api_key=";
    private static final String SOMETHING_IN_GENRES = "&include_adult=false&sort_by=created_at.asc";

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

    public static String getSimilarMovie(int idMovie, int page) {
        return SITE + idMovie + "/similar" + "?api_key=" + MOVIE_KEY + LANGUAGE + PAGE + page;
    }

    public static String getMovieGenres(int idGenres, int page) {
        return SITE_GENRES
                + idGenres
                + "/movies?api_key="
                + MOVIE_KEY
                + LANGUAGE
                + SOMETHING_IN_GENRES
                + PAGE
                + page;
    }

    public static String getActor(int idActor, int page) {
        return SITE_ACTOR
                + idActor
                + "/movie_credits?api_key="
                + MOVIE_KEY
                + LANGUAGE
                + PAGE
                + page;
    }

    public static String getActorDetail(int idActor) {
        return SITE_ACTOR + idActor + "?api_key=" + MOVIE_KEY + LANGUAGE;
    }
}
