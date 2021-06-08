package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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
        if (toolbar != null) {
            toolbar.setTitle("Упражнение");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        taskName.setText(name);
        task.setText(description);
    }

    public void initializeMenu() {
        IProfile profile;
        User user = MyApplication.getInstance().user;
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
        } else {
            Drawer result = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(accountHeader)
                    .withActionBarDrawerToggleAnimated(true)
                    .withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_main))
                    .addDrawerItems(
                            new PrimaryDrawerItem()
                                    .withName("Вход")
                                    .withIcon(R.drawable.ic_baseline_person_24)
                                    .withTextColor(Color.WHITE)
                                    .withSetSelected(true)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                            startActivity(intent);
                            return true;
                        }
                    })
                    .build();
        }
    }
}
