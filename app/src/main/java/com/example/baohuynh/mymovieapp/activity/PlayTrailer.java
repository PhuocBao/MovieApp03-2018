package com.example.baohuynh.mymovieapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.CommentAdapter;
import com.example.baohuynh.mymovieapp.adapter.MovieAdapter;
import com.example.baohuynh.mymovieapp.data.MovieAPI;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import com.example.baohuynh.mymovieapp.handler.CallBackOnClickItem;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieJson;
import com.example.baohuynh.mymovieapp.handler.CallbackMovieTrailer;
import com.example.baohuynh.mymovieapp.handler.GetMovieJson;
import com.example.baohuynh.mymovieapp.handler.GetMovieTrailerJson;
import com.example.baohuynh.mymovieapp.handler.OnLoadMoreListener;
import com.example.baohuynh.mymovieapp.model.Comment;
import com.example.baohuynh.mymovieapp.model.Movie;
import com.example.baohuynh.mymovieapp.model.Trailer;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.GoogleButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayTrailer extends AppCompatActivity
        implements CallbackMovieTrailer, CallbackMovieJson, OnLoadMoreListener,
        CallBackOnClickItem, View.OnClickListener, DialogInterface.OnShowListener {
    public static final int SIMILAR_MOVIES = 77;
    public static final String COMMENT_CHILD = "comment";
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final int RC_SIGN_IN = 7;
    private CircleImageView mUserImage;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<Trailer> mTrailers = new ArrayList<>();
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private ArrayList<Comment> mComments = new ArrayList<>();
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerMovie, mRecyclerComment;
    private AlertDialog mDialogLogin;
    private TextView mTvMoreComment, mTvEmptyComment;
    private int idMovie;
    private String movieName;
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);
        //init Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        initView();
        //configure google sign in
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                        getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        idMovie = getIntent().getIntExtra(MovieDetail.MOVIE_ID, 1);
        movieName = getIntent().getStringExtra(MovieDetail.NAME);
        GetMovieTrailerJson getMovieTrailerJson = new GetMovieTrailerJson(mTrailers, this);
        getMovieTrailerJson.execute(MovieAPI.getTrailerMovie(idMovie));
        getSimilarMovie(mPage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).into(mUserImage);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUserImage.setImageDrawable(getDrawable(R.drawable.ic_empty_user));
            }
        }
        final ArrayList<Comment> mTempComments = new ArrayList<>();
        mDatabase.child(COMMENT_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(String.valueOf(idMovie))) {
                    mDatabase.child(COMMENT_CHILD).child(String.valueOf(idMovie)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //clear comment list before run for loop
                            mComments.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Comment comment = ds.getValue(Comment.class);
                                //clear temp list and add new everytime mComments size > 2
                                mTempComments.clear();
                                mComments.add(comment);
                                if (mComments.size() > 2) {
                                    mTvMoreComment.setVisibility(View.VISIBLE);
                                    //mTvEmptyComment.setVisibility(View.GONE);
                                    for (int i = 0; i < mComments.size(); i++) {
                                        //if mComments size > 2 use mTempComments
                                        mTempComments.add(mComments.get(i));
                                        if (i == 2) break;
                                    }
                                    mRecyclerComment.setAdapter(new CommentAdapter(getApplicationContext(), mTempComments));
                                    mRecyclerComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                } else {
                                    mTvEmptyComment.setVisibility(View.GONE);
                                    mTvMoreComment.setVisibility(View.INVISIBLE);
                                    mRecyclerComment.setAdapter(new CommentAdapter(getApplicationContext(), mComments));
                                    mRecyclerComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    mDatabase.child(COMMENT_CHILD).setValue(idMovie);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    @Override
    public void getTrailerSuccess(final ArrayList<Trailer> trailers) {
        YouTubePlayerFragment playerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.fragment_youtubeview);
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
        playerFragment.initialize(MovieAPI.YOUTUBE_API, onInitializedListener);
    }

    @Override
    public void getTrailerFail(Throwable e) {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(ArrayList<Movie> movies) {
        mRecyclerMovie = findViewById(R.id.recycler_similar_movie);
        ArrayList<Movie> mTempList = new ArrayList<>();
        if (movies != null) {
            for (int i = 0; i < movies.size(); i++) {
                mTempList.add(movies.get(i));
                if (i == 8) break;
            }
            GridLayoutManager gridLayoutManager =
                    new GridLayoutManager(this, MovieFragment.SPAN_COUNT);
            mRecyclerMovie.setLayoutManager(gridLayoutManager);
            mMovieAdapter = new MovieAdapter(this, mTempList, mRecyclerMovie);
            mRecyclerMovie.setAdapter(mMovieAdapter);
            mMovieAdapter.setClickMovieItem(this);
            mMovieAdapter.setLoadMoreListener(this);
        }
//        } else {
//            //if arraylist not null, insert item into progress loading position = size - 1
//            //the list will not reload and back to top of view
//            mMovieAdapter.notifyItemInserted(mTempList.size() - 1);
//            mMovieAdapter.setLoading(false);
//        }
    }

    @Override
    public void onFail(Throwable e) {
//        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        mMovies.add(null);
        mRecyclerMovie.post(new Runnable() {
            @Override
            public void run() {
                //when progress is loading, remove progress position when it loaded
                //if not, the progress always in loading status
                int positionLoading = mMovies.size() - 1;
                mMovies.remove(positionLoading);
                mMovieAdapter.notifyItemRemoved(positionLoading);
            }
        });
        mPage++;
        getSimilarMovie(mPage);
        mMovieAdapter.setLoading(true);
    }

    @Override
    public void onClick(int position) {
        Intent iDetailMovie = new Intent(this, MovieDetail.class);
        iDetailMovie.putParcelableArrayListExtra(MovieFragment.MOVIE_LIST, mMovies);
        iDetailMovie.putExtra(MovieFragment.POSITION, position);
        startActivity(iDetailMovie);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_movie:
                Intent iGenres = new Intent(this, Genres.class);
                iGenres.putExtra(MainActivity.GENRES_KEY, SIMILAR_MOVIES);
                iGenres.putExtra(MovieDetail.MOVIE_ID, idMovie);
                iGenres.putExtra(MovieDetail.NAME, movieName);
                startActivity(iGenres);
                break;
            case R.id.btn_comment:
                if (mAuth.getCurrentUser() == null) {
                    showDialogLogin();
                } else {
                    moveToComment();
                }
                break;
            case R.id.btn_loginFB:
                handleFbLogin();
                break;
            case R.id.btn_loginGG:
                handleGgSignIn();
                break;
            case R.id.tv_more_comment:
                moveToComment();
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        Window window = ((AlertDialog) dialogInterface).getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void initView() {
        mUserImage = findViewById(R.id.img_user);
        mTvMoreComment = findViewById(R.id.tv_more_comment);
        mTvMoreComment.setOnClickListener(this);
        mTvMoreComment.setVisibility(View.INVISIBLE);
        mTvEmptyComment = findViewById(R.id.tv_empty_comment);
        TextView tvMoreMovie = findViewById(R.id.tv_more_movie);
        tvMoreMovie.setOnClickListener(this);
        Button btnComment = findViewById(R.id.btn_comment);
        btnComment.setOnClickListener(this);
        mRecyclerComment = findViewById(R.id.recycler_comment);
    }

    private void getSimilarMovie(int page) {
        GetMovieJson getSimilarMovieJson = new GetMovieJson(mMovies, this);
        getSimilarMovieJson.execute(MovieAPI.getSimilarMovie(idMovie, page));
    }

    private void showDialogLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_login, null);
        FacebookButton btnFbLogin = view.findViewById(R.id.btn_loginFB);
        btnFbLogin.setOnClickListener(this);
        GoogleButton btnGgSignin = view.findViewById(R.id.btn_loginGG);
        btnGgSignin.setOnClickListener(this);
        builder.setView(view);
        mDialogLogin = builder.create();
        mDialogLogin.setOnShowListener(this);
        mDialogLogin.show();
    }

    private void handleGgSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFbLogin() {
        List<String> per = Arrays.asList("public_profile", "email");
        LoginManager.getInstance().logInWithReadPermissions(this, per);
        LoginManager.getInstance()
                .registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(PlayTrailer.this, "Login Canceled", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(PlayTrailer.this, "Login Error", Toast.LENGTH_SHORT).show();
                        Log.d("error", "onError: " + error.getMessage());
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d("token", "handleFacebookAccessToken:" + token);
//        mProgressDialogFb = new ProgressDialog(this);
//        mProgressDialogFb.setMessage("Waiting for login..");
//        mProgressDialogFb.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUI();
//                            mProgressDialogFb.dismiss();
                            mDialogLogin.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(PlayTrailer.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final ProgressDialog progressDialogGg = new ProgressDialog(this);
        progressDialogGg.setMessage("Waiting for login..");
        progressDialogGg.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(PlayTrailer.this, "Login Success", Toast.LENGTH_SHORT)
                                    .show();
                            updateUI();
                            progressDialogGg.dismiss();
                            mDialogLogin.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(PlayTrailer.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI() {
        if (mAuth.getCurrentUser() != null) {
            Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.ic_empty_user).into(mUserImage);
        }
    }

    private void moveToComment() {
        Intent iComment = new Intent(getApplicationContext(), CommentActivity.class);
        iComment.putExtra(MovieDetail.MOVIE_ID, idMovie);
        iComment.putExtra(MovieDetail.NAME, movieName);
        startActivity(iComment);
    }
}
