package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.adapter.UsersAdapter;
import com.example.logoapplication.crud.TeacherCRUD;
import com.example.logoapplication.crud.UserCRUD;
import com.example.logoapplication.entities.Teacher;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.bson.types.ObjectId;

public class UserLIstActivity extends AppCompatActivity {
    CardView selectUsers;
    CardView selectTeachers;
    RecyclerView userList;
    UsersAdapter usersAdapter;
    UserCRUD userCRUD;
    TeacherCRUD teacherCRUD;
    ProgressBar progressBar;
    Toolbar toolbar;

    UsersAdapter.OnDeleteUserListener onDeleteUserListener = position -> {
        if(usersAdapter.getUsers().size()==0){
            Teacher teacher = usersAdapter.getTeachers().get(position);
            teacherCRUD.deleteTeacher(teacher.getId(), teacher1 -> {
                progressBar.setVisibility(View.VISIBLE);
                userList.setVisibility(View.GONE);
                teacherCRUD.getAllTeachers(teachers -> {
                    progressBar.setVisibility(View.GONE);
                    usersAdapter.setTeachers(teachers);
                    userList.setVisibility(View.VISIBLE);
                });
            });
        } else {
            ObjectId userId = usersAdapter.getUsers().get(position).getId();
            userCRUD.deleteUser(userId, user -> {
                progressBar.setVisibility(View.VISIBLE);
                userList.setVisibility(View.GONE);
                userCRUD.getAllUsers(users -> {
                    progressBar.setVisibility(View.GONE);
                    usersAdapter.setUsers(users);
                    userList.setVisibility(View.VISIBLE);
                });
            });
        }
    };

    UsersAdapter.OnClickUser onClickUser = position -> {
        Intent intent = new Intent(UserLIstActivity.this, ProfileActivity.class);
        if(usersAdapter.getUsers().size()==0){
            MyApplication.getInstance().additionalTeacherProfile = usersAdapter.getTeachers().get(position);
        } else {
            MyApplication.getInstance().additionalUserProfile = usersAdapter.getUsers().get(position);
        }
        startActivity(intent);
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);
        selectUsers = findViewById(R.id.selectUsers);
        progressBar = findViewById(R.id.loading_spinner);
        selectTeachers = findViewById(R.id.selectTeachers);
        userList = findViewById(R.id.recyclerViewUsers);
        userList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        userList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        usersAdapter = new UsersAdapter(onDeleteUserListener, onClickUser);
        userList.setAdapter(usersAdapter);
        userCRUD = new UserCRUD(null);
        teacherCRUD = new TeacherCRUD(null);
        userCRUD.getAllUsers(users -> {
            progressBar.setVisibility(View.GONE);
            userList.setVisibility(View.VISIBLE);
            usersAdapter.setUsers(users);
        });
        selectUsers.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            userList.setVisibility(View.GONE);
            userCRUD.getAllUsers(users -> {
                progressBar.setVisibility(View.GONE);
                usersAdapter.setUsers(users);
                userList.setVisibility(View.VISIBLE);
            });
        });
        selectTeachers.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            userList.setVisibility(View.GONE);
            teacherCRUD.getAllTeachers(teachers -> {
                progressBar.setVisibility(View.GONE);
                usersAdapter.setTeachers(teachers);
                userList.setVisibility(View.VISIBLE);
            });
        });
        toolbar = findViewById(R.id.maintoolbar);
        if (toolbar != null) {
            toolbar.setTitle("Главное меню");
            setSupportActionBar(toolbar);
            initializeMenu();
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
                                        .withSetSelected(true),
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
                                if(position == 1) {
                                    Intent intent = new Intent(UserLIstActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 2){
                                    Intent intent = new Intent(UserLIstActivity.this, UserLIstActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 3){
                                    Intent intent = new Intent(UserLIstActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if(position == 4){
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(UserLIstActivity.this, MainActivity.class);
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
                                        .withSetSelected(true),
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
                                if(position == 1) {
                                    Intent intent = new Intent(UserLIstActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 2) {
                                    Intent intent = new Intent(UserLIstActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 3) {
                                    Intent intent = new Intent(UserLIstActivity.this, ChatListActivity.class);
                                    startActivity(intent);
                                }
                                if (position == 4) {
                                    MyApplication.getInstance().user = null;
                                    MyApplication.getInstance().teacher = null;
                                    Intent intent = new Intent(UserLIstActivity.this, MainActivity.class);
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
                            if(position == 2) {
                                Intent intent = new Intent(UserLIstActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            return true;
                        }
                    })
                    .build();
        }
    }
}
