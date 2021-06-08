package com.example.logoapplication.activity;

import android.os.Bundle;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    TextView userFIO;
    TextView userLocation;
    TextView userDescription;
    TextView userMail;
    TextView userPass;
    TextView userDate;
    ImageView userDocs;
    MaterialButton button;
    TextView text_docs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        userFIO = findViewById(R.id.userFio);
        userLocation = findViewById(R.id.userLocation);
        userDescription = findViewById(R.id.userAbout);
        userMail = findViewById(R.id.userMail);
        userPass = findViewById(R.id.userPass);
        userDate = findViewById(R.id.userDate);
        userDocs = findViewById(R.id.userDocs1);
        text_docs = findViewById(R.id.text_doc);
        button = findViewById(R.id.redact_button); // добавить клик листенер
        if(MyApplication.getInstance().user != null){
            fillUserViews();
        } else {
            fillTeacherViews();
        }

    }

    private void fillUserViews(){
        User user = MyApplication.getInstance().user;
        userFIO.setText(user.getName());
        userLocation.setText(user.getLocation());
        ((ViewManager)userDescription.getParent()).removeView(userDescription);
        userMail.setText(user.getEmail());
        userPass.setText(user.getPassword());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        userDate.setText(formatter.format(user.getDate()));
        ((ViewManager)userDocs.getParent()).removeView(userDocs);
        ((ViewManager)text_docs.getParent()).removeView(text_docs);
    }

    private void fillTeacherViews(){
        Teacher teacher = MyApplication.getInstance().teacher;
        userFIO.setText(teacher.getName());
        userLocation.setText(teacher.getLocation());
        userDescription.setText(teacher.getDescription());
        userMail.setText(teacher.getEmail());
        userPass.setText(teacher.getPassword());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        userDate.setText(formatter.format(teacher.getDate()));
        ((ViewManager)userDocs.getParent()).removeView(userDocs); //пока что
    }
}
