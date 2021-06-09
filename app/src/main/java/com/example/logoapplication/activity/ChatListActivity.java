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
import com.example.logoapplication.adapter.ChatsAdapter;
import com.example.logoapplication.crud.ChatsCRUD;
import com.example.logoapplication.entities.Chat;
import com.example.logoapplication.entities.Teacher;
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

public class ChatListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatsAdapter chatsAdapter;
    Toolbar toolbar;
    ProgressBar progressBar;

    ChatsAdapter.OnChatClickListener onChatClickListener = new ChatsAdapter.OnChatClickListener() {
        @Override
        public void onClick(int position) {
            Chat chat = chatsAdapter.getChat(position);
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("id", chat.getId());
            if(MyApplication.getInstance().user!=null){
                intent.putExtra("receiver", chat.getId_teacher());
            }else {
                intent.putExtra("receiver", chat.getId_user());
            }
            startActivity(intent);
        }
    };

    ChatsCRUD.OnChatChange onChatChange = new ChatsCRUD.OnChatChange() {
        @Override
        public void onChange(List<Chat> chat) {
            progressBar.setVisibility(View.INVISIBLE);
            ((ViewManager) progressBar.getParent()).removeView(progressBar);
            chatsAdapter.setChats(chat);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_layout);
        recyclerView = findViewById(R.id.recyclerViewChats);
        toolbar = findViewById(R.id.maintoolbar);
        progressBar = findViewById(R.id.loading_spinner_chat);
        if (toolbar != null) {
            toolbar.setTitle("Чаты");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        chatsAdapter = new ChatsAdapter(onChatClickListener);
        recyclerView.setAdapter(chatsAdapter);
        ChatsCRUD chatsCRUD = new ChatsCRUD(onChatChange);
        if(MyApplication.getInstance().user!=null){
            ObjectId user = MyApplication.getInstance().user.getId();
            chatsCRUD.getChatsByProfile(new Document("id_user", user));
        } else {
            ObjectId teacher = MyApplication.getInstance().teacher.getId();
            chatsCRUD.getChatsByProfile(new Document("id_teacher", teacher));
        }
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
                            if(position == 1){
                                Intent intent = new Intent(ChatListActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            if(position == 2){
                                Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                            if(position == 3){
                                Intent intent = new Intent(ChatListActivity.this, ChatListActivity.class);
                                startActivity(intent);
                            }
                            if(position == 4){
                                MyApplication.getInstance().user = null;
                                MyApplication.getInstance().teacher = null;
                                Intent intent = new Intent(ChatListActivity.this, MainActivity.class);
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
                                Intent intent = new Intent(ChatListActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }
}
