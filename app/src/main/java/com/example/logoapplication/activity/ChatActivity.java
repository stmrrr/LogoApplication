package com.example.logoapplication.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.ChatsAdapter;
import com.example.logoapplication.adapter.MessagesAdapter;
import com.example.logoapplication.crud.MessageCRUD;
import com.example.logoapplication.entities.Message;
import com.example.logoapplication.entities.Teacher;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    EditText editText;
    ImageButton imageButton;
    MessagesAdapter messagesAdapter;
    MessageCRUD messageCRUD;
    ObjectId id;

    MessageCRUD.OnChangeMessages onChangeMessages = new MessageCRUD.OnChangeMessages() {
        @Override
        public void onChange(List<Message> messages) {
            messagesAdapter.setMessages(messages);
        }
    };

    MessageCRUD.Listener listener = new MessageCRUD.Listener() {
        @Override
        public void onChange() {
            List<Message> messages = messageCRUD.getMessagesByChatIdSync(new Document("chat_id", id));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messagesAdapter.setMessages(messages);
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView = findViewById(R.id.recyclerViewMessages);
        toolbar = findViewById(R.id.maintoolbar);
        if (toolbar != null) {
            toolbar.setTitle("Чаты");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        editText = findViewById(R.id.m);
        imageButton = findViewById(R.id.sendMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        messagesAdapter = new MessagesAdapter();
        recyclerView.setAdapter(messagesAdapter);
        id = (ObjectId) getIntent().getSerializableExtra("id");
        messageCRUD = new MessageCRUD(onChangeMessages, listener);
        messageCRUD.getMessagesByChatId(new Document("chat_id", id));
        messageCRUD.watchForChanges();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = editText.getText().toString();
        ObjectId from;
        ObjectId to = (ObjectId) getIntent().getSerializableExtra("receiver");
        if(MyApplication.getInstance().user!=null){
            from = MyApplication.getInstance().user.getId();
        } else{
            from = MyApplication.getInstance().teacher.getId();
        }
        Message message = new Message(
                new ObjectId(),
                id,
                messageText,
                from,
                to
        );
        messageCRUD.insertMessage(message);
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
                                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            if(position == 2){
                                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                            if(position == 3){
                                Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
                                startActivity(intent);
                            }
                            if(position == 4){
                                MyApplication.getInstance().user = null;
                                MyApplication.getInstance().teacher = null;
                                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
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
                                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }
}
