package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.User;

import org.bson.Document;

import io.realm.mongodb.mongo.MongoCollection;

public class UserCRUD {

    private final UserLoginListener userLoginListener;

    public interface UserLoginListener{
        void onLogin(User user);
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
}
