package com.example.logoapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.logoapplication.R;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class TaskActivity extends AppCompatActivity {
    TextView taskName;
    TextView task;
    Toolbar toolbar;
    MaterialButton materialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);
        taskName = findViewById(R.id.taskName);
        task = findViewById(R.id.task);
        toolbar = findViewById(R.id.task_toolbar);
        materialButton = findViewById(R.id.complete_task);
        if(toolbar!=null){
            toolbar.setTitle("Упражнение");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        taskName.setText(name);
        task.setText(description);
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
