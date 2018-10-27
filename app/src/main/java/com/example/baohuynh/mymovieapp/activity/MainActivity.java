package com.example.baohuynh.mymovieapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.annotation.Retention;
import java.util.Objects;

import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.ACTION;
import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.ADVENTURE;
import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.ANIMATION;
import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.COMEDY;
import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.CRIME;
import static com.example.baohuynh.mymovieapp.activity.MainActivity.Nav_Item.DRAMA;
import static com.example.baohuynh.mymovieapp.activity.PlayTrailer.mAuth;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String GENRES_KEY = "genres_key";
    public static final int ACTION_ID = 28;
    public static final int ADVENTURE_ID = 12;
    public static final int ANIMATION_ID = 16;
    public static final int COMEDY_ID = 35;
    public static final int CRIME_ID = 80;
    public static final int DRAMA_ID = 18;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
        initDrawerNavigation();
    }

    private void initDrawerNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Home");
        }
        mDrawerLayout = findViewById(R.id.drawer_main);
        ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                        R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager_main);
        TabLayout tabLayout = findViewById(R.id.tablayout_main);
        viewPager.setAdapter(new ViewPagerAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent iGenres = new Intent(this, Genres.class);
        switch (item.getItemId()) {
            case R.id.menu_action:
                iGenres.putExtra(GENRES_KEY, ACTION_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_adventure:
                iGenres.putExtra(GENRES_KEY, ADVENTURE_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_animation:
                iGenres.putExtra(GENRES_KEY, ANIMATION_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_comedy:
                iGenres.putExtra(GENRES_KEY, COMEDY_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_crime:
                iGenres.putExtra(GENRES_KEY, CRIME_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_drama:
                iGenres.putExtra(GENRES_KEY, DRAMA_ID);
                startActivity(iGenres);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_logout:
                logout();
                mDrawerLayout.closeDrawers();
                break;
        }
        return true;
    }

    private void logout() {
        if (mAuth.getCurrentUser() != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                    getString(R.string.default_web_client_id)).requestEmail().build();
            GoogleSignInClient signInClient =
                    GoogleSignIn.getClient(this, gso);
            signInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                }
            });
            mAuth.signOut();
            LoginManager.getInstance().logOut();
        }
    }

    @Retention(SOURCE)
    @IntDef({ACTION, ADVENTURE, ANIMATION, COMEDY, CRIME, DRAMA})
    public @interface Nav_Item {
        int ACTION = 0;
        int ADVENTURE = 1;
        int ANIMATION = 2;
        int COMEDY = 3;
        int CRIME = 4;
        int DRAMA = 5;
    }
}
