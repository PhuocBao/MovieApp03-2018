package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;
import com.example.baohuynh.mymovieapp.model.Trailer;
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
 * Created by baohuynh on 30/03/2018.
 */

public class GetMovieTrailerJson extends AsyncTask<String, Void, String> {
    private static final String RESULT = "results";
    private static final String TRAILER_ID = "id";
    private static final String TRAILER_KEY = "key";
    private ArrayList<Trailer> mTrailers;
    private CallbackMovieTrailer mMovieTrailer;

    public GetMovieTrailerJson(ArrayList<Trailer> trailers, CallbackMovieTrailer movieTrailer) {
        mTrailers = trailers;
        mMovieTrailer = movieTrailer;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String url = strings[0];
        try {
            URL trailerUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) trailerUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = trailerUrl.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String read = "";
            while ((read = bufferedReader.readLine()) != null) {
                result += read;
            }
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            mMovieTrailer.getTrailerFail(e);
        } catch (IOException e) {
            mMovieTrailer.getTrailerFail(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObjectTrailer = new JSONObject(s);
            JSONArray jsonArrayResults = jsonObjectTrailer.getJSONArray(RESULT);
            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject jsonObject = jsonArrayResults.getJSONObject(i);
                String id = jsonObject.getString(TRAILER_ID);
                String key = jsonObject.getString(TRAILER_KEY);
                mTrailers.add(new Trailer(id, key));
            }
            mMovieTrailer.getTrailerSuccess(mTrailers);
        } catch (JSONException e) {
            mMovieTrailer.getTrailerFail(e);
        }
        super.onPostExecute(s);
    }
}
