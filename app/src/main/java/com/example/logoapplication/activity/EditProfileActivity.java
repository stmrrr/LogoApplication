package com.example.logoapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;

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

import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText editName;
    TextInputEditText editSurname;
    TextInputEditText editAbout;
    TextInputEditText editDate;
    TextInputEditText editLocation;
    TextInputEditText editEmail;
    TextInputEditText editPassword;
    TextInputEditText editRepeatPassword;
    MaterialButton editButton;

    UserCRUD.UserLoginListener userLoginListener = new UserCRUD.UserLoginListener() {
        @Override
        public void onLogin(User user) {
            finish();
        }
    };

    TeacherCRUD.TeacherLoginListener teacherLoginListener = new TeacherCRUD.TeacherLoginListener() {
        @Override
        public void onLogin(Teacher teacher) {
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        editName = findViewById(R.id.edit_name);
        editSurname = findViewById(R.id.edit_surname);
        editAbout = findViewById(R.id.edit_about);
        editDate = findViewById(R.id.edit_date);
        editLocation = findViewById(R.id.edit_location);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editRepeatPassword = findViewById(R.id.edit_repeat_password);
        editButton = findViewById(R.id.edit_button);

        if(MyApplication.getInstance().user != null){
            fillUserViews();
        } else {
            fillTeacherViews();
        }

        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
    }

    private void fillUserViews(){
        User user = MyApplication.getInstance().user;
        String name = user.getName().split(" ")[0];
        String surname = user.getName().split(" ")[1];
        editName.setText(name);
        editSurname.setText(surname);
        ((ViewManager)editAbout.getParent()).removeView(editAbout);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        editDate.setText(formatter.format(user.getDate()));
        editLocation.setText(user.getLocation());
        editEmail.setText(user.getEmail());
        editPassword.setText(user.getPassword());
        editRepeatPassword.setText(user.getPassword());
    }

    private void fillTeacherViews(){
        Teacher teacher = MyApplication.getInstance().teacher;
        String name = teacher.getName().split(" ")[0];
        String surname = teacher.getName().split(" ")[1];
        editName.setText(name);
        editSurname.setText(surname);
        editAbout.setText(teacher.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        editDate.setText(formatter.format(teacher.getDate()));
        editLocation.setText(teacher.getLocation());
        editEmail.setText(teacher.getEmail());
        editPassword.setText(teacher.getPassword());
        editRepeatPassword.setText(teacher.getPassword());
    }

    private void editProfile(){
        if(editPassword.getText().toString().equals(editRepeatPassword.getText().toString())) {
            if (MyApplication.getInstance().user != null) {
                editUser();
            } else {
                editTeacher();
            }
        }
    }

    private void editUser(){
        User user = MyApplication.getInstance().user;
        String name = editName.getText().toString();
        String surname = editSurname.getText().toString();
        String date = editDate.getText().toString();
        Date date1 = user.getDate();
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        String location = editLocation.getText().toString();
        String email = editEmail.getText().toString();
        String password = editRepeatPassword.getText().toString();
        ObjectId id = user.getId();
        Document query = new Document("id", id);
        Document update = new Document("name", name+" "+surname).append("date", date1).append("location", location)
                .append("email", email).append("password", password);
        UserCRUD userCRUD = new UserCRUD(userLoginListener);
        userCRUD.updateUser(query, update);
    }

    private void editTeacher(){
        User user = MyApplication.getInstance().user;
        String name = editName.getText().toString();
        String surname = editSurname.getText().toString();
        String date = editDate.getText().toString();
        Date date1 = user.getDate();
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        String location = editLocation.getText().toString();
        String email = editEmail.getText().toString();
        String password = editRepeatPassword.getText().toString();
        String about = editAbout.getText().toString();
        ObjectId id = user.getId();
        Document query = new Document("id", id);
        Document update = new Document("name", name+" "+surname).append("date", date1).append("location", location)
                .append("email", email).append("password", password).append("description", about);
        TeacherCRUD teacherCRUD = new TeacherCRUD(teacherLoginListener);
        teacherCRUD.updateTeacher(query, update);
    }
}