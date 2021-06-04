package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.entities.SubSection;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SubSectionCRUD {
    private final MongoDatabase mongoDatabase;
    private final CodecRegistry codecRegistry;

    public SubSectionCRUD(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        this.mongoDatabase = mongoDatabase;
        this.codecRegistry = codecRegistry;
    }

    public List<SubSection> getSubSectionsByID(ObjectId id){
        MongoCollection<SubSection> mongoCollection =
                mongoDatabase.getCollection(
                        "subsection",
                        SubSection.class).withCodecRegistry(codecRegistry);
        RealmResultTask<MongoCursor<SubSection>> findTask = mongoCollection.find().iterator();
        List<SubSection> subsections = new ArrayList<>();
        MongoCursor<SubSection> results = findTask.get();
        while (results.hasNext()) {
            SubSection section = results.next();
            Log.v("EXAMPLE", section.toString());
            if(section.getSectionId().equals(id)) {
                subsections.add(section);
            }
        }

        return subsections;
    }
}
