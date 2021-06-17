package com.example.logoapplication.activity;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.ChatsAdapter;
import com.example.logoapplication.adapter.MessagesAdapter;
import com.example.logoapplication.crud.ChatsCRUD;
import com.example.logoapplication.crud.CompletedTaskCRUD;
import com.example.logoapplication.crud.ExerciseCRUD;
import com.example.logoapplication.crud.MessageCRUD;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Exercise;
import com.example.logoapplication.entities.Message;
import com.example.logoapplication.entities.Teacher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    EditText editText;
    ImageButton imageButton;
    MessagesAdapter messagesAdapter;
    ImageButton selectFile;
    MessageCRUD messageCRUD;
    ObjectId id;
    MaterialButton accessButton;

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

    MessagesAdapter.OnCLickMessage messagesOnClick = new MessagesAdapter.OnCLickMessage() {
        @Override
        public void downloadFile(int position) {
            Message message = messagesAdapter.getMessage(position);
            String path = message.getAttachment();
            download(path);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        accessButton = findViewById(R.id.accessButton);
        boolean access = getIntent().getBooleanExtra("access", false);
        recyclerView = findViewById(R.id.recyclerViewMessages);
        toolbar = findViewById(R.id.maintoolbar);
        if (toolbar != null) {
            toolbar.setTitle("Чаты");
            setSupportActionBar(toolbar);
            initializeMenu();
        }
        editText = findViewById(R.id.m);
        imageButton = findViewById(R.id.sendMessage);
        selectFile = findViewById(R.id.selectFile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        messagesAdapter = new MessagesAdapter(messagesOnClick);
        recyclerView.setAdapter(messagesAdapter);
        id = (ObjectId) getIntent().getSerializableExtra("id");
        messageCRUD = new MessageCRUD(onChangeMessages, listener);
        messageCRUD.getMessagesByChatId(new Document("chat_id", id));
        messageCRUD.watchForChanges();
        if (access && MyApplication.getInstance().teacher != null) {
            accessButton.setVisibility(View.VISIBLE);
            accessButton.setOnClickListener(view -> getAccess());
        }
        imageButton.setOnClickListener(view -> sendMessage());
        selectFile.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            upload(uri);
        }
    }

    private void upload(Uri uri){
        StorageReference storageRef = MyApplication.getInstance().firebaseStorage.getReference();
        StorageReference storageReference = storageRef.child("video").child(uri.getLastPathSegment());
        try {
            UploadTask task = storageReference.putFile(uri);
            task.continueWithTask(task1 -> {
                if (!task1.isSuccessful()) {
                    throw task1.getException();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task12 -> {
                if(task12.isSuccessful()){
                    String downloadedUri = task12.getResult().toString();
                    String[] ur = task12.getResult().getLastPathSegment().split("/");
                    String text = ur[ur.length-1];
                    ObjectId from;
                    ObjectId to = (ObjectId) getIntent().getSerializableExtra("receiver");
                    if (MyApplication.getInstance().user != null) {
                        from = MyApplication.getInstance().user.getId();
                    } else {
                        from = MyApplication.getInstance().teacher.getId();
                    }
                    Message message = new Message(
                            new ObjectId(),
                            id,
                            text,
                            from,
                            to,
                            downloadedUri
                    );
                    messageCRUD.insertMessage(message);
                }
            });
        }catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void download(String path){
        StorageReference storageRef = MyApplication.getInstance().firebaseStorage.getReferenceFromUrl(path);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String[] ur = uri.getLastPathSegment().split("/");
                String fileName = ur[ur.length-1];
                downloadFile(ChatActivity.this, fileName, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), uri.toString());
            }
        });
    }

    public void downloadFile(Context context, String fileName, String destination, String url){
        Log.d("DOWNLOAD", "Start Download");
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destination, fileName);

        downloadManager.enqueue(request);

    }

    private void sendMessage() {
        String messageText = editText.getText().toString();
        ObjectId from;
        ObjectId to = (ObjectId) getIntent().getSerializableExtra("receiver");
        if (MyApplication.getInstance().user != null) {
            from = MyApplication.getInstance().user.getId();
        } else {
            from = MyApplication.getInstance().teacher.getId();
        }
        Message message = new Message(
                new ObjectId(),
                id,
                messageText,
                from,
                to,
                ""
        );
        messageCRUD.insertMessage(message);
    }

    private void getAccess() {
        ObjectId to = (ObjectId) getIntent().getSerializableExtra("receiver");
        CompletedTaskCRUD completedTaskCRUD = new CompletedTaskCRUD(completedTasks -> {
            CompletedTask lastTask = completedTasks.get(completedTasks.size() - 1);
            ObjectId lastTaskId = lastTask.getTaskId();
            ExerciseCRUD exerciseCRUD = new ExerciseCRUD(exercises -> {
                Exercise exercise = exercises.get(0);
                ExerciseCRUD exerciseCRUD1 = new ExerciseCRUD(exercises1 -> {
                    if(exercises1.size()!=0) {
                        Exercise exercise1 = exercises1.get(0);
                        CompletedTask completedTask = new CompletedTask(
                                new ObjectId(),
                                exercise1.getId(),
                                to,
                                "uncomleted"
                        );
                        CompletedTaskCRUD completedTaskCRUD1 = new CompletedTaskCRUD(null);
                        completedTaskCRUD1.insertCompletedTask(completedTask);
                        ChatsCRUD chatsCRUD = new ChatsCRUD(null);
                        chatsCRUD.updateChat(new Document("_id", id), new Document("id_user", to)
                                .append("id_teacher", MyApplication.getInstance().teacher.getId()).append("access", false));
                    }
                });
                exerciseCRUD1.getExercise(new Document("number", exercise.getNumber() + 1)
                        .append("subsectionID", exercise.getSubsectionID()));
            });
            exerciseCRUD.getExercise(new Document("_id", lastTaskId));
        });
        completedTaskCRUD.getCompletedTaskByUser(to);
        accessButton.setVisibility(View.GONE);
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
            if(user!=null && user.getStatus().equals("ADMIN")){
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
                                        .withName("Пользователи")
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
                                if (position == 1) {
                                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 2){
                                    Intent intent = new Intent(ChatActivity.this, UserLIstActivity.class);
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
                                if (position == 1) {
                                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 2) {
                                    Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 3) {
                                    Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 4) {
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                return true;
                            }
                        })
                        .build();
            }
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
                            if (position == 2) {
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
