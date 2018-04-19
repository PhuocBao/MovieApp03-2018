package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baohuynh on 20/03/2018.
 */

public class GetMovieJson extends AsyncTask<String, Void, String> {
    public static final String ID = "id";
    public static final String RESULT = "results";
    public static final String TITLE = "title";
    public static final String POSTER = "poster_path";
    public static final String BACKDROP = "backdrop_path";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DAY = "release_date";
    private ArrayList<Movie> mMovies;
    private CallbackMovieJson mCallbackMovieJson;

    public GetMovieJson(ArrayList<Movie> movies, CallbackMovieJson callbackMovieJson) {
        mMovies = movies;
        mCallbackMovieJson = callbackMovieJson;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream(), Charset.defaultCharset()));
            String read = "";
            while ((read = bufferedReader.readLine()) != null) {
                result += read;
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            mCallbackMovieJson.CallbackFail(e);
        } catch (IOException e) {
            mCallbackMovieJson.CallbackFail(e);
        }
        return result;
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
            mCallbackMovieJson.CallbackSuccess(mMovies);
        } catch (JSONException e) {
            mCallbackMovieJson.CallbackFail(e);
        }
        super.onPostExecute(s);
    }
}
