package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.entities.Exercise;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ExerciseCRUD {
    private final MongoDatabase mongoDatabase;
    private final CodecRegistry codecRegistry;

    public ExerciseCRUD(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        this.mongoDatabase = mongoDatabase;
        this.codecRegistry = codecRegistry;
    }

    public List<Exercise> getExerciseById(ObjectId id){
        MongoCollection<Exercise> mongoCollection =
                mongoDatabase.getCollection(
                        "exercise",
                        Exercise.class).withCodecRegistry(codecRegistry);
        RealmResultTask<MongoCursor<Exercise>> findTask = mongoCollection.find().iterator();
        List<Exercise> exercises = new ArrayList<>();
        MongoCursor<Exercise> results = findTask.get();
        while (results.hasNext()) {
            Exercise section = results.next();
            Log.v("EXAMPLE", section.toString());
            if(section.getSubsectionID().equals(id)) {
                exercises.add(section);
            }
        }

        return exercises;
    }
}
