package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import org.bson.Document;

import io.realm.mongodb.mongo.MongoCollection;

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
}
