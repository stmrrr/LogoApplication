package com.example.logoapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toolbar;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.crud.SectionCRUD;
import com.example.logoapplication.entities.Section;
import com.example.logoapplication.entities.SubSection;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

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

public class MainActivity extends AppCompatActivity {
    Toolbar maintoolbar;
    SectionAdapter sectionAdapter;
    RecyclerView recyclerView;

    SectionAdapter.SectionClickListener sectionClickListener = new SectionAdapter.SectionClickListener(){
        @Override
        public void onClickSection(int position) {
            Section section = sectionAdapter.getSection(position);
            ObjectId id = section.getId();
            Log.v("INFO", id.toString());
            openSubSection(id);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maintoolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setActionBar(maintoolbar);
        maintoolbar.setTitle("Главная страница");
        recyclerView = findViewById(R.id.recyclerViewSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sectionAdapter = new SectionAdapter(sectionClickListener);
        recyclerView.setAdapter(sectionAdapter);
        SectionCRUD sectionCRUD = new SectionCRUD(MyApplication.getInstance().mongoDatabase, MyApplication.getInstance().pojoCodecRegistry);
        sectionAdapter.setSections(sectionCRUD.getSections());
    }

    public void openSubSection(ObjectId id){
        Intent intent = new Intent(MainActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}