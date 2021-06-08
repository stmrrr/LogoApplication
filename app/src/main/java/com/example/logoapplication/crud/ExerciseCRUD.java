package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Exercise;
import com.example.logoapplication.entities.Section;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ExerciseCRUD {

    ExerciseOnChangeListener exerciseOnChangeListener;

    public interface ExerciseOnChangeListener{
        void onChange(List<Exercise> exercises);
    }

    public ExerciseCRUD(ExerciseOnChangeListener exerciseOnChangeListener) {
        this.exerciseOnChangeListener = exerciseOnChangeListener;
    }

    public void getExercise(Document document){
        MongoCollection<Exercise> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "exercise",
                        Exercise.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<Exercise>> findTask = mongoCollection.find(document).iterator();
        findTask.getAsync(it -> {
            List<Exercise> exercises = new ArrayList<>();
            if(it.isSuccess()){
                MongoCursor<Exercise> results = it.get();
                while (results.hasNext()) {
                    Exercise exercise = results.next();
                    exercises.add(exercise);
                }
                exerciseOnChangeListener.onChange(exercises);
            } else{
                Log.e("EXAMPLE", "failed to find documents with: ", it.getError());
            }
        });
    }
}
