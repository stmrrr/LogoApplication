package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.adapter.ExercisesAdapter;
import com.example.logoapplication.crud.ExerciseCRUD;
import com.example.logoapplication.entities.Exercise;
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

public class ExercisesActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ExercisesAdapter exercisesAdapter;
    Toolbar toolbar;
    ProgressBar progressBar;

    ExercisesAdapter.ExerciseOnClickListener exerciseOnClickListener = new ExercisesAdapter.ExerciseOnClickListener() {
        @Override
        public void onClickExercise(int position) {
            Exercise exercise = exercisesAdapter.getExercise(position);
            openExercise(exercise);
        }
    };

    ExerciseCRUD.ExerciseOnChangeListener exerciseOnChangeListener = new ExerciseCRUD.ExerciseOnChangeListener() {
        @Override
        public void onChange(List<Exercise> exercises) {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            ((ViewManager)progressBar.getParent()).removeView(progressBar);
            exercisesAdapter.setExercises(exercises);
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
        if(toolbar!=null){
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

    public void openExercise(Exercise exercise){
        Intent intent = new Intent(ExercisesActivity.this, TaskActivity.class);
        intent.putExtra("name", "Упражнение " + exercise.getNumber());
        intent.putExtra("description", exercise.getDescription());
        intent.putExtra("id", exercise.getId());
        startActivity(intent);
    }
}
