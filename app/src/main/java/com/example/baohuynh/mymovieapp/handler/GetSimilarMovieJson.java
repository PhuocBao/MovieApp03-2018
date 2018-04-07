package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by baohuynh on 05/04/2018.
 */

public class GetSimilarMovieJson extends AsyncTask<String, Void, String> {
    private ArrayList<Movie> mMovies;
    private CallbackGetSimilarMovie mSimilarMovie;

    public GetSimilarMovieJson(ArrayList<Movie> movies, CallbackGetSimilarMovie similarMovie) {
        mMovies = movies;
        mSimilarMovie = similarMovie;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String read = "";
            while ((read = bufferedReader.readLine()) != null) {
                result += read;
            }
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObjectSimilar = new JSONObject(s);
            JSONArray jsonArrayResults = jsonObjectSimilar.getJSONArray(GetMovieJson.RESULT);
            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject jsonObjectResults = jsonArrayResults.getJSONObject(i);
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
            mSimilarMovie.onGetSimilarMovieSuccess(mMovies);
        } catch (JSONException e) {
            mSimilarMovie.onGetSimilarMovieFail(e);
        }
        super.onPostExecute(s);
    }
}
