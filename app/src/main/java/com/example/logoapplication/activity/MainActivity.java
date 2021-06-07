package com.example.logoapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.adapter.SectionClickListener;
import com.example.logoapplication.crud.SectionCRUD;
import com.example.logoapplication.entities.Section;

import org.bson.Document;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MainActivity extends AppCompatActivity {
    SectionAdapter sectionAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    AtomicReference<User> user = new AtomicReference<>();

    SectionClickListener sectionClickListener = new SectionClickListener(){
        @Override
        public void onClickSection(int position) {
            Section section = sectionAdapter.getSection(position);
            ObjectId id = section.getId();
            String mark = section.getMark();
            Log.v("INFO", id.toString());
            openSubSection(id, mark);
        }
    };

    SectionCRUD.SectionChange sectionChange = new SectionCRUD.SectionChange() {
        @Override
        public void onChange(List<Section> sections) {
            progressBar.setVisibility(View.INVISIBLE);
            ((ViewManager)progressBar.getParent()).removeView(progressBar);
            recyclerView.setVisibility(View.VISIBLE);
            sectionAdapter.setSections(sections);
        }
    };

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.loading_spinner);
        recyclerView = findViewById(R.id.recyclerViewSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sectionAdapter = new SectionAdapter(sectionClickListener);
        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setVisibility(View.INVISIBLE);
        initializeDatabaseConnection();
    }

    public void openSubSection(ObjectId id, String mark){
        Intent intent = new Intent(MainActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("mark", mark);
        startActivity(intent);
    }

    public void initializeDatabaseConnection(){
        App app = new App(new AppConfiguration.Builder("logo-iefok").build());
        Credentials anonymousCredentials = Credentials.anonymous();
        app.loginAsync(anonymousCredentials, it -> {
            if(it.isSuccess()){
                Log.v("AUTH", "Successfully authenticated anonymously.");
                user.set(app.currentUser());
                createDatabase();
                SectionCRUD sectionCRUD = new SectionCRUD(sectionChange);
                sectionCRUD.getSections(new Document("id_main_section", null));
            } else {
                Log.e("AUTH", it.getError().toString());
            }
        });
    }

    public void createDatabase(){
        MongoClient mongoClient = user.get().getMongoClient("mongodb-atlas");
        MyApplication.getInstance().mongoDatabase =
                mongoClient.getDatabase("logotrener-database");
        MyApplication.getInstance().pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
}