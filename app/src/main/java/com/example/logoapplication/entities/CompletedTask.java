package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class CompletedTask {
    private ObjectId id;
    private ObjectId taskId;
    private ObjectId userId;

    public CompletedTask(ObjectId id, ObjectId taskId, ObjectId userId) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTaskId() {
        return taskId;
    }

    public void setTaskId(ObjectId taskId) {
        this.taskId = taskId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
