package com.example.logoapplication;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.crud.SectionCRUD;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MyApplication extends Application {
    public MongoDatabase mongoDatabase;
    public CodecRegistry pojoCodecRegistry;
    public User user;
    public Teacher teacher;

    private static MyApplication instance;

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
