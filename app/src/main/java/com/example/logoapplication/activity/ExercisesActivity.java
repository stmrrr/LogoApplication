package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.ExercisesAdapter;
import com.example.logoapplication.crud.CompletedTaskCRUD;
import com.example.logoapplication.crud.ExerciseCRUD;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Exercise;
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

import java.util.List;

public class ExercisesActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ExercisesAdapter exercisesAdapter;
    Toolbar toolbar;
    ProgressBar progressBar;
    List<CompletedTask> completedTasks;

    ExercisesAdapter.ExerciseOnClickListener exerciseOnClickListener = new ExercisesAdapter.ExerciseOnClickListener() {
        @Override
        public void onClickExercise(int position, boolean flag) {
            Exercise exercise = exercisesAdapter.getExercise(position);
            openExercise(exercise, flag);
        }
    };

    ExerciseCRUD.ExerciseOnChangeListener exerciseOnChangeListener = new ExerciseCRUD.ExerciseOnChangeListener() {
        @Override
        public void onChange(List<Exercise> exercises) {
            CompletedTaskCRUD completedTaskCRUD = new CompletedTaskCRUD(completedTask -> {
                exercisesAdapter.setCompletedTasks(completedTask);
                completedTasks = completedTask;
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                ((ViewManager) progressBar.getParent()).removeView(progressBar);
                exercisesAdapter.setExercises(exercises);
            });
            ObjectId userId = MyApplication.getInstance().user!=null?MyApplication.getInstance().user.getId():new ObjectId();
            completedTaskCRUD.getCompletedTaskByUser(userId);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_layout);
        recyclerView = findViewById(R.id.recyclerViewExersises);
        toolbar = findViewById(R.id.toolbar_exercises);
        progressBar = findViewById(R.id.spinner_exercises);
        String name = getIntent().getStringExtra("name");
        if (toolbar != null) {
            toolbar.setTitle(name);
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        exercisesAdapter = new ExercisesAdapter(exerciseOnClickListener);
        recyclerView.setAdapter(exercisesAdapter);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        ExerciseCRUD exerciseCRUD = new ExerciseCRUD(exerciseOnChangeListener);
        exerciseCRUD.getExercise(new Document("subsectionID", id));
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
                                    Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 2){
                                    Intent intent = new Intent(ExercisesActivity.this, UserLIstActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 3){
                                    Intent intent = new Intent(ExercisesActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 4){
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
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
                                    Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 2) {
                                    Intent intent = new Intent(ExercisesActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 3) {
                                    Intent intent = new Intent(ExercisesActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 4) {
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
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
                                Intent intent = new Intent(ExercisesActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }

    public void openExercise(Exercise exercise, boolean flag) {
        Intent intent = new Intent(ExercisesActivity.this, TaskActivity.class);
        intent.putExtra("name", "Упражнение " + exercise.getNumber());
        intent.putExtra("description", exercise.getDescription());
        intent.putExtra("flag", flag);
        intent.putExtra("id", exercise.getId());
        startActivity(intent);
    }
}
