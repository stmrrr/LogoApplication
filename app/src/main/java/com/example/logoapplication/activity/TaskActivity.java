package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.crud.ChatsCRUD;
import com.example.logoapplication.crud.CompletedTaskCRUD;
import com.example.logoapplication.crud.MessageCRUD;
import com.example.logoapplication.entities.Chat;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Message;
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

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class TaskActivity extends AppCompatActivity {
    TextView taskName;
    TextView task;
    Toolbar toolbar;
    MaterialButton materialButton;
    String description;
    ObjectId id;

    ChatsCRUD.OnChatChange chatChange = new ChatsCRUD.OnChatChange() {
        @Override
        public void onChange(List<Chat> chat) {
            for(Chat chat1 : chat){
                if(chat1.getId_teacher().equals(MyApplication.getInstance().user.getTeacherId())){
                    ObjectId id = chat1.getId();
                    MessageCRUD messageCRUD = new MessageCRUD(null, null);
                    messageCRUD.insertMessage(new Message(
                            new ObjectId(),
                            id,
                            "Ваш ученик закончил упражнение \"" + description.substring(0, 50) + "...\"",
                            MyApplication.getInstance().user.getId(),
                            chat1.getId_teacher()
                    ));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);
        taskName = findViewById(R.id.taskName);
        task = findViewById(R.id.task);
        toolbar = findViewById(R.id.task_toolbar);
        materialButton = findViewById(R.id.complete_task);
        id = (ObjectId) getIntent().getSerializableExtra("id");
        if (toolbar != null) {
            toolbar.setTitle("Упражнение");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        if(MyApplication.getInstance().user == null){
            ((ViewManager)materialButton.getParent()).removeView(materialButton);
        } else {
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completeTask();
                }
            });
        }
        String name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        taskName.setText(name);
        task.setText(description);
    }

    private void completeTask() {
        CompletedTaskCRUD completedTaskCRUD = new CompletedTaskCRUD(completedTask -> {
            materialButton.setText("Выполнено");
            ChatsCRUD chatsCRUD = new ChatsCRUD(chatChange);
            chatsCRUD.getChatsByProfile(new Document("id_user", MyApplication.getInstance().user.getId()));
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        });
        completedTaskCRUD.insertCompletedTask(new CompletedTask(new ObjectId(), id, MyApplication.getInstance().user.getId()));
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
                            if(position == 1){
                                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            if(position == 2){
                                Intent intent = new Intent(TaskActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                            if(position == 3){
                                Intent intent = new Intent(TaskActivity.this, ChatListActivity.class);
                                startActivity(intent);
                            }
                            if(position == 4){
                                MyApplication.getInstance().user = null;
                                MyApplication.getInstance().teacher = null;
                                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
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
                                Intent intent = new Intent(TaskActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }
}
