package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.adapter.LetterAdapter;
import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.adapter.SectionAdapterInterface;
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
import org.bson.types.ObjectId;

import java.util.List;

public class SubSectionActivity extends AppCompatActivity {
    SectionAdapterInterface sectionAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Toolbar toolbar;

    SectionClickListener sectionClickListener = new SectionClickListener(){
        @Override
        public void onClickSection(int position) {
            Section section = sectionAdapter.getSection(position);
            ObjectId id = section.getId();
            String name = section.getName();
            boolean isEnd = section.getEnd();
            if(isEnd){
                openExercise(id);
            }else {
                String mark = section.getMark();
                openSubSection(id, mark, name);
            }
        }
    };

    SectionCRUD.SectionChange sectionChange = new SectionCRUD.SectionChange() {
        @Override
        public void onChange(List<Section> sections) {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            ((ViewManager)progressBar.getParent()).removeView(progressBar);
            sectionAdapter.setSections(sections);
        }
    };

    public void openExercise(ObjectId id){
        Intent intent = new Intent(SubSectionActivity.this, ExerciseActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subsection_layout);
        progressBar = findViewById(R.id.loading_spinner_subsection);
        String name = getIntent().getStringExtra("name");
        String mark = getIntent().getStringExtra("mark");
        if(mark.equals("sound")){
            name = "Звук " + name;
        }
        toolbar = findViewById(R.id.toolbar_subsection);
        if(toolbar!=null){
            toolbar.setTitle(name);
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        recyclerView = findViewById(R.id.recyclerViewSubSection);
        if (mark.equals("sounds")){
            sectionAdapter = new LetterAdapter(sectionClickListener);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            sectionAdapter = new SectionAdapter(sectionClickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
        recyclerView.setAdapter((RecyclerView.Adapter)sectionAdapter);
        recyclerView.setVisibility(View.INVISIBLE);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        SectionCRUD sectionCRUD = new SectionCRUD(sectionChange);
        sectionCRUD.getSections(new Document("id_main_section", id));
    }

    public void openSubSection(ObjectId id, String mark, String name){
        Intent intent = new Intent(SubSectionActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("mark", mark);
        intent.putExtra("name", name);
        startActivity(intent);
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
