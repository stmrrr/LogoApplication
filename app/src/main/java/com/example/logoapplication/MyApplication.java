package com.example.logoapplication;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.example.logoapplication.crud.SectionCRUD;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MyApplication extends Application {
    AtomicReference<User> user = new AtomicReference<User>();
    public MongoDatabase mongoDatabase;
    public CodecRegistry pojoCodecRegistry;

    private static MyApplication instance;

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        instance = this;
        App app = new App(new AppConfiguration.Builder("logo-iefok").build());

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Credentials anonymousCredentials = Credentials.anonymous();
        user.set(app.login(anonymousCredentials));
        createDatabase();
    }

    public void createDatabase(){
        MongoClient mongoClient = user.get().getMongoClient("mongodb-atlas");
        mongoDatabase =
                mongoClient.getDatabase("logotrener-database");
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        SectionCRUD sectionCRUD = new SectionCRUD(mongoDatabase, pojoCodecRegistry);
        sectionCRUD.getSections();
    }
}
