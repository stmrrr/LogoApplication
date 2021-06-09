package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Message;

import org.bson.BsonObjectId;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmEventStreamAsyncTask;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MessageCRUD {

    private final OnChangeMessages onChangeMessages;
    private final Listener listener;

    public MessageCRUD(OnChangeMessages onChangeMessages, Listener listener) {
        this.onChangeMessages = onChangeMessages;
        this.listener = listener;
    }

    public interface OnChangeMessages{
        void onChange(List<Message> messages);
    }

    public interface Listener{
        void onChange();
    }

    public void getMessagesByChatId(Document document){
        MongoCollection<Message> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "message",
                        Message.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<Message>> findTask = mongoCollection.find(document).iterator();
        findTask.getAsync(it -> {
            List<Message> messages = new ArrayList<>();
            if(it.isSuccess()){
                MongoCursor<Message> results = it.get();
                while (results.hasNext()) {
                    Message message = results.next();
                    messages.add(message);
                }
                onChangeMessages.onChange(messages);
            } else{
                Log.e("EXAMPLE", "failed to find documents with: ", it.getError());
            }
        });
    }

    public List<Message> getMessagesByChatIdSync(Document document){
        MongoCollection<Message> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "message",
                        Message.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        MongoCursor<Message> results = mongoCollection.find(document).iterator().get();
        List<Message> messages = new ArrayList<>();
        while (results.hasNext()) {
            Message message = results.next();
            messages.add(message);
        }
        return messages;
    }

    public void watchForChanges(){
        MongoCollection<Message> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "message",
                        Message.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmEventStreamAsyncTask<Message> watcher = mongoCollection.watchAsync();
        watcher.get(it -> {
            if(it.isSuccess()){
                listener.onChange();
            }else{
                Log.e("EXAMPLE", "failed to find documents with: ", it.getError());
            }
        });
    }

    public void insertMessage(Message message){
        MongoCollection<Message> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "message",
                        Message.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        mongoCollection.insertOne(message).getAsync(task -> {
            if (task.isSuccess()) {
                BsonObjectId insertedId = task.get().getInsertedId().asObjectId();
                Log.v("EXAMPLE", "successfully inserted a document with id " + insertedId);
            } else {
                Log.e("EXAMPLE", "failed to insert document with: ", task.getError());
            }
        });
    }
}
