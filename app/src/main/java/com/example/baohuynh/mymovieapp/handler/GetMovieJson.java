package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;

import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by baohuynh on 20/03/2018.
 */

public class GetMovieJson extends AsyncTask<String, Void, String> {
    static final String ID = "id";
    static final String RESULT = "results";
    static final String TITLE = "title";
    static final String POSTER = "poster_path";
    static final String BACKDROP = "backdrop_path";
    static final String VOTE_AVERAGE = "vote_average";
    static final String OVERVIEW = "overview";
    static final String RELEASE_DAY = "release_date";
    private ArrayList<Movie> mMovies;
    private CallbackMovieJson mCallbackMovieJson;

    public GetMovieJson(ArrayList<Movie> movies, CallbackMovieJson callbackMovieJson) {
        mMovies = movies;
        mCallbackMovieJson = callbackMovieJson;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream(), Charset.defaultCharset()));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                result.append(read);
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            mCallbackMovieJson.onFail(e);
        } catch (IOException e) {
            //mCallbackMovieJson.CallbackFail(e);
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray(RESULT);
            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                int id = object.getInt(ID);
                String name = object.getString(TITLE);
                String poster = MovieAPI.GET_IMAGE_PATH + object.getString(POSTER);
                String backdrop = MovieAPI.GET_IMAGE_PATH + object.getString(BACKDROP);
                String overview = object.getString(OVERVIEW);
                Double rate = object.getDouble(VOTE_AVERAGE);
                String releaseDay = object.getString(RELEASE_DAY);
                mMovies.add(new Movie(id, poster, name, overview, backdrop, releaseDay, rate));
            }
            mCallbackMovieJson.onSuccess(mMovies);
        } catch (JSONException e) {
            mCallbackMovieJson.onFail(e);
        }
        super.onPostExecute(s);
    }
}
