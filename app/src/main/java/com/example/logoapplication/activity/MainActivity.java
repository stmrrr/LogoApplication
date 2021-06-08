package com.example.logoapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.adapter.SectionClickListener;
import com.example.logoapplication.crud.SectionCRUD;
import com.example.logoapplication.entities.Section;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

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
    Toolbar toolbar;
    AtomicReference<User> user = new AtomicReference<>();

    SectionClickListener sectionClickListener = new SectionClickListener(){
        @Override
        public void onClickSection(int position) {
            Section section = sectionAdapter.getSection(position);
            ObjectId id = section.getId();
            Boolean isEnd = section.getIsEnd();
            String name = section.getName();
            Log.v("INFO", id.toString());
            if(isEnd){
                openExercise(id, name);
            }else {
                String mark = section.getMark();
                openSubSection(id, mark, name);
            }
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
        toolbar = findViewById(R.id.maintoolbar);
        if(toolbar!=null){
            toolbar.setTitle("Главное меню");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        initializeDatabaseConnection();
    }

    public void openSubSection(ObjectId id, String mark, String name){
        Intent intent = new Intent(MainActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("mark", mark);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void openExercise(ObjectId id, String name){
        Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
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

    public void initializeMenu(){
        IProfile profile = new ProfileDrawerItem()
                .withName("Фамилия Имя Отчество")
                .withEmail("user@mail.com")
                .withIcon(R.drawable.ic_baseline_person_24);

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withTextColor(Color.WHITE)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_main))
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Личный кабинет")
                                .withIcon(R.drawable.ic_baseline_person_24)
                                .withTextColor(Color.WHITE)
                                .withSetSelected(true),
                        new PrimaryDrawerItem()
                                .withName("Чаты")
                                .withIcon(R.drawable.ic_baseline_chat_24)
                                .withTextColor(Color.WHITE),
                        new PrimaryDrawerItem()
                                .withName("Выход")
                                .withTextColor(Color.WHITE)
                )
                .build();
    }
}