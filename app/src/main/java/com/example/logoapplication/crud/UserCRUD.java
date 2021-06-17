package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Section;
import com.example.logoapplication.entities.User;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class UserCRUD {

    private final UserLoginListener userLoginListener;

    public interface UserLoginListener{
        void onLogin(User user);
    }

    public interface AllUsersListener{
        void onUsers(List<User> users);
    }

    public UserCRUD(UserLoginListener userLoginListener) {
        this.userLoginListener = userLoginListener;
    }

    public void loginUser(Document document){
        MongoCollection<User> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "user", User.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.findOne(document).getAsync(task -> {
            if(task.isSuccess()){
                User user = task.get();
                userLoginListener.onLogin(user);
            } else {
                Log.e("EXAMPLE", "failed to find document with: ", task.getError());
            }
        });
    }

    public void updateUser(Document query, Document update){
        MongoCollection<User> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "user", User.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.updateOne(query, update).getAsync(it -> {
            if(it.isSuccess()){
                long count = it.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                    userLoginListener.onLogin(MyApplication.getInstance().user);
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", it.getError());
            }
        });
    }

    public void insertUser(User user){
        MongoCollection<User> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "user", User.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.insertOne(user).getAsync(task -> {
            if(task.isSuccess()){
                userLoginListener.onLogin(user);
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    public void getAllUsers(AllUsersListener allUsersListener){
        MongoCollection<User> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "user", User.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<User>> findTask = mongoCollection.find().iterator();
        findTask.getAsync(task -> {
            if(task.isSuccess()){
                MongoCursor<User> cursor = task.get();
                List<User> users = new ArrayList<>();
                while(cursor.hasNext()){
                    User user = cursor.next();
                    users.add(user);
                }
                allUsersListener.onUsers(users);
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }

    public void deleteUser(ObjectId userId, UserLoginListener userLoginListener){
        MongoCollection<User> mongoCollection = MyApplication.getInstance().mongoDatabase.getCollection(
                "user", User.class)
                .withCodecRegistry(com.example.logoapplication.MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.deleteOne(new Document("_id", userId)).getAsync(task -> {
            if (task.isSuccess()) {
                long count = task.get().getDeletedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully deleted a document.");
                    userLoginListener.onLogin(null);
                } else {
                    Log.v("EXAMPLE", "did not delete a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to delete document with: ", task.getError());
            }
        });
    }
}
