package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TeacherCRUD {
    private final TeacherLoginListener teacherLoginListener;

    public interface TeacherLoginListener{
        void onLogin(Teacher teacher);
    }

    public TeacherCRUD(TeacherLoginListener teacherLoginListener) {
        this.teacherLoginListener = teacherLoginListener;
    }

    public void loginTeacher(Document document){
        MongoCollection<Teacher> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "teacher", Teacher.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.findOne(document).getAsync(task -> {
            if(task.isSuccess()){
                Teacher teacher = task.get();
                teacherLoginListener.onLogin(teacher);
            } else {
                Log.e("EXAMPLE", "failed to find document with: ", task.getError());
            }
        });
    }

    public void updateTeacher(Document query, Document update){
        MongoCollection<Teacher> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "teacher", Teacher.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.updateOne(query, update).getAsync(it -> {
            if(it.isSuccess()){
                long count = it.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                    teacherLoginListener.onLogin(MyApplication.getInstance().teacher);
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", it.getError());
            }
        });
    }

    public void insertTeacher(Teacher teacher){
        MongoCollection<Teacher> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "teacher", Teacher.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.insertOne(teacher).getAsync(task -> {
            if(task.isSuccess()){
                teacherLoginListener.onLogin(teacher);
            }else{
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }
}
