package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Exercise;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CompletedTaskCRUD {
    private Listener listener;

    public interface Listener{
        void onComplete(CompletedTask completedTask);
    }

    public CompletedTaskCRUD(Listener listener) {
        this.listener = listener;
    }

    public void insertCompletedTask(CompletedTask completedTask){
        MongoCollection<CompletedTask> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "completed_task",
                        CompletedTask.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.insertOne(completedTask).getAsync(task -> {
            if(task.isSuccess()){
                listener.onComplete(completedTask);
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }
}
