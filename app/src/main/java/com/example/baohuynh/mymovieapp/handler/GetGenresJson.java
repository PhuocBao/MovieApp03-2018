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
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baohuynh on 07/04/2018.
 */

public class GetGenresJson extends AsyncTask<String, Void, String> {
    private ArrayList<Movie> mMovies;
    private CallbackMovieJson mCallbackGenres;

    public GetGenresJson(ArrayList<Movie> movies, CallbackMovieJson callbackGenres) {
        mMovies = movies;
        mCallbackGenres = callbackGenres;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                result.append(read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObjectGenres = new JSONObject(s);
            JSONArray jsonArray = jsonObjectGenres.getJSONArray(GetMovieJson.RESULT);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectResults = jsonArray.getJSONObject(i);
                String backDropPath = MovieAPI.GET_IMAGE_PATH + jsonObjectResults.getString(
                        GetMovieJson.BACKDROP);
                int id = jsonObjectResults.getInt(GetMovieJson.ID);
                String overView = jsonObjectResults.getString(GetMovieJson.OVERVIEW);
                String releaseDate = jsonObjectResults.getString(GetMovieJson.RELEASE_DAY);
                String posterPath =
                        MovieAPI.GET_IMAGE_PATH + jsonObjectResults.getString(GetMovieJson.POSTER);
                String name = jsonObjectResults.getString(GetMovieJson.TITLE);
                Double vote = jsonObjectResults.getDouble(GetMovieJson.VOTE_AVERAGE);
                mMovies.add(
                        new Movie(id, posterPath, name, overView, backDropPath, releaseDate, vote));
            }
            mCallbackGenres.onSuccess(mMovies);
        } catch (JSONException e) {
            mCallbackGenres.onFail(e);
        }
        super.onPostExecute(s);
    }
}
