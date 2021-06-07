package com.example.logoapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class SubSectionActivity extends AppCompatActivity {
    SectionAdapterInterface sectionAdapter;
    RecyclerView recyclerView;

    SectionClickListener sectionClickListener = new SectionClickListener(){
        @Override
        public void onClickSection(int position) {
            Section section = sectionAdapter.getSection(position);
            ObjectId id = section.getId();
            Boolean isEnd = section.getEnd();
            if(isEnd){
                openExercise(id);
            }else {
                String mark = section.getMark();
                openSubSection(id, mark);
            }
        }
    };

    SectionCRUD.SectionChange sectionChange = new SectionCRUD.SectionChange() {
        @Override
        public void onChange(List<Section> sections) {
            recyclerView.setVisibility(View.VISIBLE);
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
        recyclerView = findViewById(R.id.recyclerViewSubSection);
        String mark = getIntent().getStringExtra("mark");
        if (mark.equals("sounds")){
            sectionAdapter = new LetterAdapter(sectionClickListener);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            sectionAdapter = new SectionAdapter(sectionClickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
        recyclerView.setAdapter((RecyclerView.Adapter)sectionAdapter);

        ObjectId id = (ObjectId) getIntent().getSerializableExtra("id");
        SectionCRUD sectionCRUD = new SectionCRUD(sectionChange);
        sectionCRUD.getSections(new Document("id_main_section", id));
    }

    public void openSubSection(ObjectId id, String mark){
        Intent intent = new Intent(SubSectionActivity.this, SubSectionActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("mark", mark);
        startActivity(intent);
    }
}
