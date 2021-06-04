package com.example.logoapplication.activity;

import android.content.Intent;
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
import com.example.logoapplication.adapter.SubSectionAdapter;
import com.example.logoapplication.crud.SubSectionCRUD;
import com.example.logoapplication.entities.SubSection;

import org.bson.types.ObjectId;

public class SubSectionActivity extends AppCompatActivity {
    Toolbar maintoolbar;
    SubSectionAdapter subSectionAdapter;
    RecyclerView recyclerView;

    SubSectionAdapter.SubSectionOnClickListener subSectionOnClickListener = new SubSectionAdapter.SubSectionOnClickListener() {
        @Override
        public void onClickSection(int position) {
            SubSection section = subSectionAdapter.getSubSection(position);
            ObjectId id = section.getId();
            Log.v("INFO", id.toString());
            openExercises(id);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subsection_layout);
        maintoolbar = findViewById(R.id.maintoolbar);
        setActionBar(maintoolbar);
        maintoolbar.setTitle("Подразделы");
        recyclerView = findViewById(R.id.recyclerViewSubSection);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subSectionAdapter = new SubSectionAdapter(subSectionOnClickListener);
        recyclerView.setAdapter(subSectionAdapter);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        SubSectionCRUD subSectionCRUD = new SubSectionCRUD(MyApplication.getInstance().mongoDatabase, MyApplication.getInstance().pojoCodecRegistry);
        subSectionAdapter.setSubSections(subSectionCRUD.getSubSectionsByID(id));
    }

    public void openExercises(ObjectId id){
        Intent intent = new Intent(SubSectionActivity.this, ExerciseActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
