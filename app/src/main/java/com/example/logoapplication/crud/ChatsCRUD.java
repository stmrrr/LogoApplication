package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Chat;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ChatsCRUD {

    private final OnChatChange onChatChange;

    public interface OnChatChange{
        void onChange(List<Chat> chat);
    }

    public ChatsCRUD(OnChatChange onChatChange) {
        this.onChatChange = onChatChange;
    }

    public void getChatsByProfile(Document document){
        MongoCollection<Chat> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "chat",
                        Chat.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<Chat>> findTask = mongoCollection.find(document).iterator();
        findTask.getAsync(it -> {
            List<Chat> chats = new ArrayList<>();
            if(it.isSuccess()){
                MongoCursor<Chat> results = it.get();
                while (results.hasNext()) {
                    Chat chat = results.next();
                    chats.add(chat);
                }
                onChatChange.onChange(chats);
            } else{
                Log.e("EXAMPLE", "failed to find documents with: ", it.getError());
            }
        });
    }
}
