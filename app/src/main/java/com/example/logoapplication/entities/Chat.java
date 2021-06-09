package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class Chat {
    private ObjectId id;
    private ObjectId id_user;
    private ObjectId id_teacher;

    public Chat (ObjectId id, ObjectId id_user, ObjectId id_teacher) {
        this.id = id;
        this.id_user = id_user;
        this.id_teacher = id_teacher;
    }

    public Chat() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId_user() {
        return id_user;
    }

    public void setId_user(ObjectId id_user) {
        this.id_user = id_user;
    }

    public ObjectId getId_teacher() {
        return id_teacher;
    }

    public void setId_teacher(ObjectId id_teacher) {
        this.id_teacher = id_teacher;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", id_teacher=" + id_teacher +
                '}';
    }
}
