package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class Exercise {
    private ObjectId id;
    private int number;
    private String description;
    private ObjectId subsectionID;
    private String name_post;

    public Exercise() {
    }

    public Exercise(ObjectId id, int number, String description, ObjectId subSectionId, String name_post) {
        this.id = id;
        this.number = number;
        this.description = description;
        this.subsectionID = subSectionId;
        this.name_post = name_post;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getSubsectionID() {
        return subsectionID;
    }

    public void setSubsectionID(ObjectId subsectionID) {
        this.subsectionID = subsectionID;
    }

    public String getName_post() {
        return name_post;
    }

    public void setName_post(String name_post) {
        this.name_post = name_post;
    }
}
