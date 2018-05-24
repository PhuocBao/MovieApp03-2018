package com.example.baohuynh.mymovieapp.handler;

import android.os.AsyncTask;
import com.example.baohuynh.mymovieapp.model.ActorDetail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

public class GetActorDetail extends AsyncTask<String, Void, String> {
    private CallbackActorDetail mCallbackActorDetail;

    public GetActorDetail(CallbackActorDetail callbackActorDetail) {
        mCallbackActorDetail = callbackActorDetail;
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
            mCallbackActorDetail.onGetActorDetailFail(e);
        } catch (IOException e) {
            mCallbackActorDetail.onGetActorDetailFail(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObjectActor = new JSONObject(s);
            String deathDay;
            String name = jsonObjectActor.getString(GetMovieActorJson.ACTOR_NAME);
            String birthDay = jsonObjectActor.getString("birthday");
            if (jsonObjectActor.has("deathday")) {
                deathDay = jsonObjectActor.getString("deathday");
            } else {
                deathDay = "null";
            }
            String bio = jsonObjectActor.getString("biography");
            String place = jsonObjectActor.getString("place_of_birth");
            ActorDetail mActorDetail = new ActorDetail(name, birthDay, bio, deathDay, place);
            mCallbackActorDetail.onGetActorDetailSuccess(mActorDetail);
        } catch (JSONException e) {
            mCallbackActorDetail.onGetActorDetailFail(e);
        }
    }
}
