package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.LetterAdapter;
import com.example.logoapplication.adapter.SectionAdapter;
import com.example.logoapplication.adapter.SectionAdapterInterface;
import com.example.logoapplication.adapter.SectionClickListener;
import com.example.logoapplication.crud.SectionCRUD;
import com.example.logoapplication.entities.Section;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubSectionActivity extends AppCompatActivity {
    SectionAdapterInterface sectionRAdapter;
    SectionAdapterInterface sectionLAdapter;
    SectionAdapterInterface sectionSAdapter;
    RecyclerView recyclerViewRSection;
    RecyclerView recyclerViewLSection;
    RecyclerView recyclerViewSSection;
    TextView r;
    TextView s;
    TextView l;
    ProgressBar progressBar;
    Toolbar toolbar;
    String mark;

    SectionClickListener sectionClickRListener = new SectionClickListener() {
        @Override
        public void onClickSection(int position) {
            Section section = sectionRAdapter.getSection(position);
            ObjectId id = section.getId();
            String name = section.getName();
            Boolean isEnd = section.getIsEnd();
            if (isEnd) {
                openExercise(id, name);
            } else {
                String mark = section.getMark();
                openSubSection(id, mark, name);
            }
        }
    };

    SectionClickListener sectionClickLListener = new SectionClickListener() {
        @Override
        public void onClickSection(int position) {
            Section section = sectionLAdapter.getSection(position);
            ObjectId id = section.getId();
            String name = section.getName();
            Boolean isEnd = section.getIsEnd();
            if (isEnd) {
                openExercise(id, name);
            } else {
                String mark = section.getMark();
                openSubSection(id, mark, name);
            }
        }
    };

    SectionClickListener sectionClickSListener = new SectionClickListener() {
        @Override
        public void onClickSection(int position) {
            Section section = sectionSAdapter.getSection(position);
            ObjectId id = section.getId();
            String name = section.getName();
            Boolean isEnd = section.getIsEnd();
            if (isEnd) {
                openExercise(id, name);
            } else {
                String mark = section.getMark();
                openSubSection(id, mark, name);
            }
        }
    };

    SectionCRUD.SectionChange sectionChange = new SectionCRUD.SectionChange() {
        @Override
        public void onChange(List<Section> sections) {
            List<Section> sSounds = new ArrayList<>();
            progressBar.setVisibility(View.INVISIBLE);
            ((ViewManager) progressBar.getParent()).removeView(progressBar);
            recyclerViewRSection.setVisibility(View.VISIBLE);
            if (mark.equals("sounds")) {
                recyclerViewLSection.setVisibility(View.VISIBLE);
                recyclerViewSSection.setVisibility(View.VISIBLE);
                for (Section section : sections) {
                    switch (section.getDescription()) {
                        case "Автоматизация звука Р":
                            sectionRAdapter.setSections(Collections.singletonList(section));
                            break;
                        case "Автоматизация звука Л":
                            sectionLAdapter.setSections(Collections.singletonList(section));
                            break;
                        case "Шипящие звуки":
                            sSounds.add(section);
                            break;
                    }
                }
                sectionSAdapter.setSections(sSounds);
            } else {
                sectionRAdapter.setSections(sections);
            }
        }
    };

    public void openExercise(ObjectId id, String name) {
        Intent intent = new Intent(SubSectionActivity.this, ExercisesActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subsection_layout);
        progressBar = findViewById(R.id.loading_spinner_subsection);
        String name = getIntent().getStringExtra("name");
        mark = getIntent().getStringExtra("mark");
        if (mark.equals("sound")) {
            name = "Звук " + name;
        }
        toolbar = findViewById(R.id.toolbar_subsection);
        if (toolbar != null) {
            toolbar.setTitle(name);
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        recyclerViewRSection = findViewById(R.id.recyclerViewRsection);
        recyclerViewLSection = findViewById(R.id.recyclerViewLsection);
        recyclerViewSSection = findViewById(R.id.recyclerViewSSounds);
        s = findViewById(R.id.S);
        r = findViewById(R.id.R);
        l = findViewById(R.id.L);
        if (mark.equals("sounds")) {
            sortSoundsAndInitializeRecyclerView();
        } else {
            ((ViewManager) s.getParent()).removeView(s);
            ((ViewManager) r.getParent()).removeView(r);
            ((ViewManager) l.getParent()).removeView(l);
            ((ViewManager) recyclerViewLSection.getParent()).removeView(recyclerViewLSection);
            ((ViewManager) recyclerViewSSection.getParent()).removeView(recyclerViewSSection);
            sectionRAdapter = new SectionAdapter(sectionClickRListener);
            recyclerViewRSection.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerViewRSection.setAdapter((RecyclerView.Adapter) sectionRAdapter);
        }
        recyclerViewRSection.setVisibility(View.INVISIBLE);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        SectionCRUD sectionCRUD = new SectionCRUD(sectionChange);
        sectionCRUD.getSections(new Document("id_main_section", id));
    }

    public void openSubSection(ObjectId id, String mark, String name) {
        Intent intent = new Intent(SubSectionActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("mark", mark);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    public void initializeMenu() {
        IProfile profile;
        com.example.logoapplication.entities.User user = MyApplication.getInstance().user;
        Teacher teacher = MyApplication.getInstance().teacher;
        if (user != null) {
            profile = new ProfileDrawerItem()
                    .withName(user.getName())
                    .withEmail(user.getEmail())
                    .withIcon(R.drawable.ic_baseline_person_24);
        } else if (teacher != null) {
            profile = new ProfileDrawerItem()
                    .withName(teacher.getName())
                    .withEmail(teacher.getEmail())
                    .withIcon(R.drawable.ic_baseline_person_24);
        } else {
            profile = new ProfileDrawerItem()
                    .withName("Анонимный пользователь")
                    .withIcon(R.drawable.ic_baseline_person_24);
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withTextColor(Color.WHITE)
                .build();
        if (user != null || teacher != null) {
            if(user!=null && user.getStatus().equals("ADMIN")){
                Drawer result = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .withAccountHeader(accountHeader)
                        .withActionBarDrawerToggleAnimated(true)
                        .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_main))
                        .addDrawerItems(
                                new PrimaryDrawerItem()
                                        .withName("Главная страница")
                                        .withIcon(R.drawable.baseline_home_24)
                                        .withTextColor(Color.WHITE),
                                new PrimaryDrawerItem()
                                        .withName("Пользователи")
                                        .withIcon(R.drawable.ic_baseline_person_24)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false),
                                new PrimaryDrawerItem()
                                        .withName("Чаты")
                                        .withIcon(R.drawable.ic_baseline_chat_24)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false),
                                new PrimaryDrawerItem()
                                        .withName("Выход")
                                        .withIcon(R.drawable.ic_logout)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false)
                        )
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                Log.v("AUTH", String.valueOf(position));
                                if (position == 1) {
                                    Intent intent = new Intent(SubSectionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 2){
                                    Intent intent = new Intent(SubSectionActivity.this, UserLIstActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 3){
                                    Intent intent = new Intent(SubSectionActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 4){
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(SubSectionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                return true;
                            }
                        })
                        .build();
            } else {
                Drawer result = new DrawerBuilder()
                        .withActivity(this)
                        .withToolbar(toolbar)
                        .withAccountHeader(accountHeader)
                        .withActionBarDrawerToggleAnimated(true)
                        .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_main))
                        .addDrawerItems(
                                new PrimaryDrawerItem()
                                        .withName("Главная страница")
                                        .withIcon(R.drawable.baseline_home_24)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false),
                                new PrimaryDrawerItem()
                                        .withName("Личный кабинет")
                                        .withIcon(R.drawable.ic_baseline_person_24)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false),
                                new PrimaryDrawerItem()
                                        .withName("Чаты")
                                        .withIcon(R.drawable.ic_baseline_chat_24)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(true),
                                new PrimaryDrawerItem()
                                        .withName("Выход")
                                        .withIcon(R.drawable.ic_logout)
                                        .withTextColor(Color.WHITE)
                                        .withSetSelected(false)
                        )
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                Log.v("AUTH", String.valueOf(position));
                                if (position == 1) {
                                    Intent intent = new Intent(SubSectionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 2) {
                                    Intent intent = new Intent(SubSectionActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 3) {
                                    Intent intent = new Intent(SubSectionActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 4) {
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(SubSectionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                return true;
                            }
                        })
                        .build();
            }
        } else {
            Drawer result = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(accountHeader)
                    .withActionBarDrawerToggleAnimated(true)
                    .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_main))
                    .addDrawerItems(
                            new PrimaryDrawerItem()
                                    .withName("Главная страница")
                                    .withIcon(R.drawable.baseline_home_24)
                                    .withTextColor(Color.WHITE),
                            new PrimaryDrawerItem()
                                    .withName("Вход")
                                    .withIcon(R.drawable.baseline_login_24)
                                    .withTextColor(Color.WHITE)
                                    .withSetSelected(false)
                    )
                    .withSelectedItemByPosition(0)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if(position == 2) {
                                Intent intent = new Intent(SubSectionActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }

    public void sortSoundsAndInitializeRecyclerView() {
        sectionRAdapter = new LetterAdapter(sectionClickRListener);
        sectionLAdapter = new LetterAdapter(sectionClickLListener);
        sectionSAdapter = new LetterAdapter(sectionClickSListener);

        recyclerViewRSection.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewLSection.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSSection.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerViewRSection.setAdapter((RecyclerView.Adapter) sectionRAdapter);
        recyclerViewLSection.setAdapter((RecyclerView.Adapter) sectionLAdapter);
        recyclerViewSSection.setAdapter((RecyclerView.Adapter) sectionSAdapter);

        recyclerViewRSection.setVisibility(View.INVISIBLE);
        recyclerViewLSection.setVisibility(View.INVISIBLE);
        recyclerViewSSection.setVisibility(View.INVISIBLE);
    }
}
