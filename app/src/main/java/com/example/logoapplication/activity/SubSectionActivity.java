package com.example.logoapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.example.logoapplication.crud.SubSectionCRUD;
import com.example.logoapplication.entities.Section;
import com.example.logoapplication.entities.SubSection;

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
            String mark = section.getMark();
            openSubSection(id, mark);
        }
    };

    SectionCRUD.SectionChange sectionChange = new SectionCRUD.SectionChange() {
        @Override
        public void onChange(List<Section> sections) {
            recyclerView.setVisibility(View.VISIBLE);
            sectionAdapter.setSections(sections);
        }
    };

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
