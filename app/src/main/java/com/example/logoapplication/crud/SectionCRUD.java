package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.entities.Section;

import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SectionCRUD{
    private final MongoDatabase mongoDatabase;
    private final CodecRegistry codecRegistry;

    public SectionCRUD(MongoDatabase mongoDatabase, CodecRegistry pojoCodecRegistry) {
        this.mongoDatabase = mongoDatabase;
        this.codecRegistry = pojoCodecRegistry;
    }

    public List<Section> getSections(){
        MongoCollection<Section> mongoCollection =
                mongoDatabase.getCollection(
                        "section",
                        Section.class).withCodecRegistry(codecRegistry);
        RealmResultTask<MongoCursor<Section>> findTask = mongoCollection.find().iterator();
        List<Section> sections = new ArrayList<>();
        MongoCursor<Section> results = findTask.get();
        while (results.hasNext()) {
            Section section = results.next();
            Log.v("EXAMPLE", section.toString());
            sections.add(section);
        }
        return sections;
    }
}
