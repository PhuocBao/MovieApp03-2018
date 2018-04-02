package com.example.baohuynh.mymovieapp.model;

/**
 * Created by baohuynh on 29/03/2018.
 */

public class Actor {
    private int actorID;
    private String imgActor, nameActor, nameCharacter;

    public Actor() {
    }

    public Actor(int actorID, String imgActor, String nameActor, String nameCharacter) {
        this.actorID = actorID;
        this.imgActor = imgActor;
        this.nameActor = nameActor;
        this.nameCharacter = nameCharacter;
    }

    public int getActorID() {
        return actorID;
    }

    public void setActorID(int actorID) {
        this.actorID = actorID;
    }

    public String getImgActor() {
        return imgActor;
    }

    public void setImgActor(String imgActor) {
        this.imgActor = imgActor;
    }

    public String getNameActor() {
        return nameActor;
    }

    public void setNameActor(String nameActor) {
        this.nameActor = nameActor;
    }

    public String getNameCharacter() {
        return nameCharacter;
    }

    public void setNameCharacter(String nameCharacter) {
        this.nameCharacter = nameCharacter;
    }
}
