package com.example.logoapplication.crud;

import android.util.Log;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.entities.Section;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SectionCRUD{
    public SectionChange sectionChange;

    public SectionCRUD(SectionChange sectionChange) {
        this.sectionChange = sectionChange;
    }

    public interface SectionChange{
        void onChange(List<Section> sections);
    }

    public void getSections(Document document){
        MongoCollection<Section> mongoCollection =
                MyApplication.getInstance().mongoDatabase.getCollection(
                        "section",
                        Section.class).withCodecRegistry(MyApplication.getInstance().pojoCodecRegistry);
        RealmResultTask<MongoCursor<Section>> findTask = mongoCollection.find(document).iterator();
        findTask.getAsync(task -> {
            List<Section> sections = new ArrayList<>();
            if (task.isSuccess()) {
                MongoCursor<Section> results = task.get();
                while (results.hasNext()) {
                    Section section = results.next();
                    Log.v("EXAMPLE", section.toString());
                    sections.add(section);
                }
                sectionChange.onChange(sections);
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }
}
