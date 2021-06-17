package com.example.logoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.crud.TeacherCRUD;
import com.example.logoapplication.crud.UserCRUD;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.bson.Document;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText email;
    TextInputEditText password;
    MaterialButton button;
    TextView registerButton;

    TeacherCRUD.TeacherLoginListener teacherLoginListener = teacher -> {
        if(teacher == null){
            Toast.makeText(LoginActivity.this, "Неверное имя пользователя или пароль", Toast.LENGTH_LONG).show();
            return;
        }
        MyApplication.getInstance().teacher = teacher;
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    };

    UserCRUD.UserLoginListener userLoginListener = user -> {
        if(user == null){
            TeacherCRUD crud = new TeacherCRUD(teacherLoginListener);
            crud.loginTeacher(new Document("email", email.getText().toString()).append("password", password.getText().toString()));
        } else {
            MyApplication.getInstance().user = user;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        button = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        button.setOnClickListener(view -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
            if(!emailStr.isEmpty() && !passwordStr.isEmpty()){
                UserCRUD userCRUD = new UserCRUD(userLoginListener);
                userCRUD.loginUser(new Document("email", emailStr).append("password", passwordStr));
            }
        });
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterTypeActivity.class);
            startActivity(intent);
        });
    }
}
