package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;

public class Teacher {
    private ObjectId id;
    private String name;
    private String email;
    private String password;
    private Boolean flag;
    private RealmList<ObjectId> student_ids;
    private int max_students;
    private RealmList<String> pictures;
    private Date date;
    private String location;
    private String description;

    public Teacher(ObjectId id, String name, String email, String password, Boolean flag, RealmList<ObjectId> student_ids, int max_students, RealmList<String> pictures, Date date, String location, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.flag = flag;
        this.student_ids = student_ids;
        this.max_students = max_students;
        this.pictures = pictures;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public Teacher() {
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public RealmList<ObjectId> getStudent_ids() {
        return student_ids;
    }

    public void setStudent_ids(RealmList<ObjectId> student_ids) {
        this.student_ids = student_ids;
    }

    public int getMax_students() {
        return max_students;
    }

    public void setMax_students(int max_students) {
        this.max_students = max_students;
    }

    public RealmList<String> getPictures() {
        return pictures;
    }

    public void setPictures(RealmList<String> pictures) {
        this.pictures = pictures;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", flag=" + flag +
                ", student_ids=" + student_ids +
                ", max_students=" + max_students +
                ", pictures=" + pictures +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
