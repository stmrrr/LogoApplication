package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TeacherCRUD {
    private final TeacherLoginListener teacherLoginListener;

    public interface TeacherLoginListener{
        void onLogin(Teacher teacher);
    }

    public interface AllTeachersListener{
        void onAllTeachers(List<Teacher> teachers);
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

    public void getAllTeachers(AllTeachersListener allTeachersListener){
        MongoCollection<Teacher> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "teacher", Teacher.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<Teacher>> findTask = mongoCollection.find().iterator();
        findTask.getAsync(task -> {
            if(task.isSuccess()){
                MongoCursor<Teacher> cursor = task.get();
                List<Teacher> teachers = new ArrayList<>();
                while(cursor.hasNext()){
                    Teacher teacher = cursor.next();
                    teachers.add(teacher);
                }
                allTeachersListener.onAllTeachers(teachers);
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }

    public void deleteTeacher(ObjectId id, TeacherLoginListener teacherLoginListener){
        MongoCollection<Teacher> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "teacher", Teacher.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.deleteOne(new Document("_id", id)).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getDeletedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully deleted a document.");
                    teacherLoginListener.onLogin(null);
                } else {
                    Log.v("EXAMPLE", "did not delete a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to delete document with: ", task.getError());
            }
        });
    }
}
