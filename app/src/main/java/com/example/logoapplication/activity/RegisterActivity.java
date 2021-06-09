package com.example.logoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.crud.TeacherCRUD;
import com.example.logoapplication.crud.UserCRUD;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmList;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText registerName;
    TextInputEditText registerSurname;
    TextInputEditText registerDate;
    TextInputEditText registerLocation;
    TextInputEditText registerEmail;
    TextInputEditText registerPassword;
    TextInputEditText registerRepeatPassword;
    MaterialButton registerButton;
    int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        type = getIntent().getIntExtra("type", 0);
        registerName = findViewById(R.id.register_name);
        registerSurname = findViewById(R.id.register_surname);
        registerDate = findViewById(R.id.register_date);
        registerLocation = findViewById(R.id.register_location);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerRepeatPassword = findViewById(R.id.register_repeat_password);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerPassword.getText().toString().equals(registerRepeatPassword.getText().toString())) {
                    createProfile();
                }
            }
        });
    }

    private void createProfile(){
        String name = registerName.getText().toString();
        String surname = registerSurname.getText().toString();
        String date = registerDate.getText().toString();
        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        String location = registerLocation.getText().toString();
        String email = registerEmail.getText().toString();
        String password = registerRepeatPassword.getText().toString();
        if(type == 0){
            UserCRUD userCRUD = new UserCRUD(user -> {
                MyApplication.getInstance().user = user;
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            });
            userCRUD.insertUser(new User(
                    new ObjectId(),
                    name +" "+ surname,
                    "SIMPLE",
                    false,
                    email,
                    password,
                    location,
                    null,
                    date1
            ));
        } else {
            TeacherCRUD teacherCRUD = new TeacherCRUD(teacher -> {
                MyApplication.getInstance().teacher = teacher;
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            });
            teacherCRUD.insertTeacher(new Teacher(
                    new ObjectId(),
                    name + " " + surname,
                    email,
                    password,
                    false,
                    new RealmList<>(),
                    5,
                    new RealmList<>(),
                    date1,
                    location,
                    ""
            ));
        }
    }
}
