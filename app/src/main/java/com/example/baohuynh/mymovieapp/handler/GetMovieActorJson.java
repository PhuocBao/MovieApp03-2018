package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.model.Actor;
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
 * Created by baohuynh on 29/03/2018.
 */

public class GetMovieActorJson extends AsyncTask<String, Void, String> {
    private static final String ID_CAST = "id";
    private static final String CAST = "cast";
    private static final String ACTOR_NAME = "name";
    private static final String CHARACTER = "character";
    private static final String IMG_CAST = "profile_path";
    private ArrayList<Actor> mActors;
    private CallbackActor mCallbackActor;

    public GetMovieActorJson(ArrayList<Actor> actors, CallbackActor callbackActor) {
        mActors = actors;
        mCallbackActor = callbackActor;
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
            mCallbackActor.onGetFail(e);
        } catch (IOException e) {
            mCallbackActor.onGetFail(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonActor = new JSONObject(s);
            JSONArray jsonArrayCast = jsonActor.getJSONArray(CAST);
            for (int i = 0; i < jsonArrayCast.length(); i++) {
                JSONObject jsonObjectDetail = jsonArrayCast.getJSONObject(i);
                int id = jsonObjectDetail.getInt(ID_CAST);
                String name = jsonObjectDetail.getString(ACTOR_NAME);
                String character = jsonObjectDetail.getString(CHARACTER);
                String img = MovieAPI.GET_IMAGE_PATH + jsonObjectDetail.getString(IMG_CAST);
                mActors.add(new Actor(id, img, name, character));
            }
            mCallbackActor.onGetSuccess(mActors);
        } catch (JSONException e) {
            mCallbackActor.onGetFail(e);
        }
        super.onPostExecute(s);
    }
}
