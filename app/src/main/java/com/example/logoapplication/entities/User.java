package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

import java.util.Date;

public class User {
    private ObjectId id;
    private String name;
    private String status;
    private Boolean flag;
    private String email;
    private String password;
    private String location;
    private Date date;

    public User(ObjectId id, String name, String status, Boolean flag, String email, String password, String location, Date date) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.flag = flag;
        this.email = email;
        this.password = password;
        this.location = location;
        this.date = date;
    }

    public User() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", FIO='" + name + '\'' +
                ", status='" + status + '\'' +
                ", flag=" + flag +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
