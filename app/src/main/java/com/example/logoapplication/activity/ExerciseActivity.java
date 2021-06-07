package com.example.logoapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.ExercisesAdapter;
import com.example.logoapplication.crud.ExerciseCRUD;
import com.example.logoapplication.entities.Exercise;

import org.bson.types.ObjectId;

public class ExerciseActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ExercisesAdapter exercisesAdapter;

    ExercisesAdapter.ExerciseOnClickListener exerciseOnClickListener = new ExercisesAdapter.ExerciseOnClickListener() {
        @Override
        public void onClickExercise(int position) {
            Exercise exercise = exercisesAdapter.getExercise(position);
            Log.v("INFO", exercise.toString());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_layout);
        recyclerView = findViewById(R.id.recyclerViewExersises);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        exercisesAdapter = new ExercisesAdapter(exerciseOnClickListener);
        recyclerView.setAdapter(exercisesAdapter);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        ExerciseCRUD exerciseCRUD = new ExerciseCRUD(MyApplication.getInstance().mongoDatabase, MyApplication.getInstance().pojoCodecRegistry);
        exercisesAdapter.setExercises(exerciseCRUD.getExerciseById(id));
    }
}
