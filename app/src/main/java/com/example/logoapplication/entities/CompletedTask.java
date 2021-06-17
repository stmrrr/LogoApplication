package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class CompletedTask {
    private ObjectId id;
    private ObjectId taskId;
    private ObjectId userId;
    private String status;
    private Boolean modified;

    public CompletedTask(ObjectId id, ObjectId taskId, ObjectId userId, String status, Boolean modified) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.status = status;
        this.modified = modified;
    }

    public CompletedTask() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getModified() {
        return modified;
    }

    public void setModified(Boolean modified) {
        this.modified = modified;
    }
}
