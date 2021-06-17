package com.example.logoapplication.crud;

import android.util.Log;
import android.widget.Toast;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Exercise;
import com.example.logoapplication.entities.Section;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class CompletedTaskCRUD {
    private Listener listener;

    public interface Listener{
        void onComplete(List<CompletedTask> completedTask);
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
                Log.d("COMPLETED", "Inserted new completed tasks");
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", task.getError());
            }
        });
    }

    public void updateTaskStatus(Document query, Document update){
        MongoCollection<CompletedTask> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "completed_task",
                        CompletedTask.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);

        mongoCollection.updateOne(query, update).getAsync(it -> {
            if(it.isSuccess()){
                long count = it.get().getModifiedCount();
                if (count == 1) {
                    Log.v("EXAMPLE", "successfully updated a document.");
                    listener.onComplete(null);
                } else {
                    Log.v("EXAMPLE", "did not update a document.");
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ", it.getError());
            }
        });
    }

    public void getCompletedTaskByUser(ObjectId userId){
        MongoCollection<CompletedTask> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "completed_task",
                        CompletedTask.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<CompletedTask>> findTask = mongoCollection.find(new Document("userId", userId)).iterator();
        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                List<CompletedTask> completedTasks = new ArrayList<>();
                MongoCursor<CompletedTask> results = task.get();
                while (results.hasNext()) {
                    CompletedTask completedTask = results.next();
                    Log.v("EXAMPLE", completedTask.toString());
                    completedTasks.add(completedTask);
                }
                listener.onComplete(completedTasks);
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }
}
