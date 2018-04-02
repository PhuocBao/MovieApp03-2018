package com.example.baohuynh.mymovieapp.activity;

import android.os.Bundle;
import android.widget.Toast;
import com.example.baohuynh.mymovieapp.MovieAPI;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieTrailer;
import com.example.baohuynh.mymovieapp.handler.GetMovieTrailerJson;
import com.example.baohuynh.mymovieapp.model.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.util.ArrayList;

public class PlayTrailer extends YouTubeBaseActivity implements CallbackMovieTrailer {
    private ArrayList<Trailer> mTrailers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);
        GetMovieTrailerJson getMovieTrailerJson = new GetMovieTrailerJson(mTrailers, this);
        getMovieTrailerJson.execute(
                MovieAPI.getTrailerMovie((getIntent().getIntExtra(MovieDetail.MOVIE_ID, 1))));
    }

    @Override
    public void getTrailerSuccess(final ArrayList<Trailer> trailers) {
        YouTubePlayerView playerView = findViewById(R.id.youtube_player);
        YouTubePlayer.OnInitializedListener onInitializedListener =
                new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        ArrayList<String> keys = new ArrayList<>();
                        for (int i = 0; i < trailers.size(); i++) {
                            keys.add(trailers.get(i).getKeyTrailer());
                        }
                        youTubePlayer.loadVideos(keys);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                            YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(PlayTrailer.this,
                                "You need to run version 4.2.16 of the mobile YouTube app (or "
                                        + "higher) to use the API.", Toast.LENGTH_LONG).show();
                    }
                };
        playerView.initialize(MovieAPI.YOUTUBE_API, onInitializedListener);
    }

    @Override
    public void getTrailerFail(Throwable e) {
        //TODO: do it later
    }
}
