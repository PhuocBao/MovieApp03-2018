package com.example.baohuynh.mymovieapp.model;

import android.net.Uri;

public class Comment {
    private String userName, userConent, userImage;

    public Comment() {
    }

    public Comment(String userName, String userConent, String userImage) {
        this.userName = userName;
        this.userConent = userConent;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserConent() {
        return userConent;
    }

    public void setUserConent(String userConent) {
        this.userConent = userConent;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
