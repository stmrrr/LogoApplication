package com.example.logoapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.StorageReference;

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
        button = findViewById(R.id.redact_button);
        if(MyApplication.getInstance().user != null){
            if(MyApplication.getInstance().user.getStatus().equals("ADMIN")){
                fillWatchedProfile();
            } else {
                fillUserViews();
            }
        } else {
            fillTeacherViews();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
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

    private void fillWatchedProfile(){
        if(MyApplication.getInstance().additionalUserProfile==null){
            fillWatchedTeacher();
        } else {
            fillWatchedUser();
        }
        ((ViewManager)button.getParent()).removeView(button);
    }

    private void fillWatchedUser(){
        User user = MyApplication.getInstance().additionalUserProfile;
        userFIO.setText(user.getName());
        userLocation.setText(user.getLocation());
        ((ViewManager)userDescription.getParent()).removeView(userDescription);
        userMail.setText(user.getEmail());
        userPass.setText(user.getPassword());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        userDate.setText(formatter.format(user.getDate()));
        ((ViewManager)userDocs.getParent()).removeView(userDocs);
        ((ViewManager)text_docs.getParent()).removeView(text_docs);
        MyApplication.getInstance().additionalUserProfile=null;
    }

    private void fillWatchedTeacher(){
        Teacher teacher = MyApplication.getInstance().additionalTeacherProfile;
        userFIO.setText(teacher.getName());
        userLocation.setText(teacher.getLocation());
        userDescription.setText(teacher.getDescription());
        userMail.setText(teacher.getEmail());
        userPass.setText(teacher.getPassword());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        userDate.setText(formatter.format(teacher.getDate()));
        downloadPictureForDocs(teacher);
        MyApplication.getInstance().additionalTeacherProfile=null;
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
        downloadPictureForDocs(teacher);
    }

    private void downloadPictureForDocs(Teacher teacher){
        StorageReference storageRef = MyApplication.getInstance().firebaseStorage.getReference();
        StorageReference path = storageRef.child(teacher.getPictures().first());
        path.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userDocs.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }
}
