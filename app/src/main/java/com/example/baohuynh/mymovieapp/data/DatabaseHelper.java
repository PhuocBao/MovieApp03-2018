package com.example.baohuynh.mymovieapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.baohuynh.mymovieapp.model.Movie;
import java.util.ArrayList;

/**
 * Created by baohuynh on 09/04/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favorite.db";
    private static final String TABLE_NAME = "favorite";
    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_NAME = "name";
    private static final String MOVIE_IMG = "image";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_BACKDROP = "backdrop";
    private static final String MOVIE_RELEASE = "release";
    private static final String MOVIE_VOTE = "vote";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String CREATE_FAV_DB = "CREATE TABLE "
            + TABLE_NAME
            + " ("
            + MOVIE_ID
            + " integer not null primary key, "
            + MOVIE_NAME
            + " text, "
            + MOVIE_IMG
            + " text, "
            + MOVIE_OVERVIEW
            + " text, "
            + MOVIE_BACKDROP
            + " text, "
            + MOVIE_RELEASE
            + " text, "
            + MOVIE_VOTE
            + " real)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        sqLiteDatabase.execSQL(CREATE_FAV_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //drop older table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //create tabale again
        onCreate(sqLiteDatabase);
    }

    public void insertData(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_ID, movie.getMovieID());
        contentValues.put(MOVIE_NAME, movie.getTxtMovieName());
        contentValues.put(MOVIE_IMG, movie.getImgMovie());
        contentValues.put(MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MOVIE_BACKDROP, movie.getBackdropPath());
        contentValues.put(MOVIE_RELEASE, movie.getReleaseDate());
        contentValues.put(MOVIE_VOTE, movie.getVoteAverage());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void deleteData(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, MOVIE_ID + " = " + movie.getMovieID(), null);
        db.close();
    }

    public ArrayList<Movie> getAllMovie() {
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase databaseHelper = this.getReadableDatabase();
        String colunms[] = {
                MOVIE_ID, MOVIE_NAME, MOVIE_IMG, MOVIE_OVERVIEW, MOVIE_BACKDROP, MOVIE_RELEASE,
                MOVIE_VOTE
        };
        Cursor cursor = databaseHelper.query(TABLE_NAME, colunms, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setMovieID(cursor.getInt(cursor.getColumnIndex(MOVIE_ID)));
                movie.setImgMovie(cursor.getString(cursor.getColumnIndex(MOVIE_IMG)));
                movie.setTxtMovieName(cursor.getString(cursor.getColumnIndex(MOVIE_NAME)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MOVIE_OVERVIEW)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MOVIE_BACKDROP)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MOVIE_RELEASE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MOVIE_VOTE)));
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        databaseHelper.close();
        return movies;
    }

    public boolean checkAlready(int idMovie) {
        SQLiteDatabase db = this.getReadableDatabase();
        int idData = 0;
        String columns[] = { MOVIE_ID };
        String where = MOVIE_ID + " = " + idMovie;
        Cursor cursor = db.query(TABLE_NAME, columns, where, null, null, null, null);
        while (cursor.moveToNext()) {
            idData = cursor.getInt(cursor.getColumnIndex(MOVIE_ID));
        }
        cursor.close();
        db.close();
        return idData == idMovie;
    }
}
