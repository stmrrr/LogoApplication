package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class SubSection {
    private ObjectId id;
    private String name;
    private ObjectId sectionId;

    public SubSection(ObjectId id, String name, ObjectId sectionId) {
        this.id = id;
        this.name = name;
        this.sectionId = sectionId;
    }

    public SubSection() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getSectionId() {
        return sectionId;
    }

    public void setSectionId(ObjectId sectionId) {
        this.sectionId = sectionId;
    }
}
