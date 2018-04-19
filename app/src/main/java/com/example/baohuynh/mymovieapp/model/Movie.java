package com.example.baohuynh.mymovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by baohuynh on 20/03/2018.
 */

public class Movie implements Parcelable {
    private int movieID;
    private String imgMovie, txtMovieName, mOverview, mBackdropPath, mReleaseDate;
    private double mVoteAverage;

    public Movie() {
    }

    public Movie(int movieID, String imgMovie, String txtMovieName, String overview, String backdropPath,
            String releaseDate, double voteAverage) {
        this.movieID = movieID;
        this.imgMovie = imgMovie;
        this.txtMovieName = txtMovieName;
        this.mOverview = overview;
        this.mBackdropPath = backdropPath;
        this.mReleaseDate = releaseDate;
        this.mVoteAverage = voteAverage;
    }

    private Movie(Parcel in) {
        movieID = in.readInt();
        imgMovie = in.readString();
        txtMovieName = in.readString();
        mOverview = in.readString();
        mBackdropPath = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getImgMovie() {
        return imgMovie;
    }

    public void setImgMovie(String imgMovie) {
        this.imgMovie = imgMovie;
    }

    public String getTxtMovieName() {
        return txtMovieName;
    }

    public void setTxtMovieName(String txtMovieName) {
        this.txtMovieName = txtMovieName;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieID);
        parcel.writeString(imgMovie);
        parcel.writeString(txtMovieName);
        parcel.writeString(mOverview);
        parcel.writeString(mBackdropPath);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mVoteAverage);
    }
}
