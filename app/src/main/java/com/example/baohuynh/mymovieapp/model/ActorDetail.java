package com.example.baohuynh.mymovieapp.model;

public class ActorDetail {
    private String fullname, birth, bio, death, place;

    public ActorDetail(String fullname, String birth, String bio, String death, String place) {
        this.fullname = fullname;
        this.birth = birth;
        this.bio = bio;
        this.death = death;
        this.place = place;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
