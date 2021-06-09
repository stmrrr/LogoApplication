package com.example.logoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logoapplication.R;
import com.google.android.material.button.MaterialButton;

public class RegisterTypeActivity extends AppCompatActivity {
    MaterialButton studentButton;
    MaterialButton teacherButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_type_layout);
        studentButton = findViewById(R.id.studentButton);
        teacherButton = findViewById(R.id.teacherButton);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTypeActivity.this, RegisterActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTypeActivity.this, RegisterActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }
}
