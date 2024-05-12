package com.example.ytuobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends Activity implements View.OnClickListener {

    Button editCourseButton,coursesButton,homeButton,reportButton,messages;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        editCourseButton = findViewById(R.id.editCourse_Button);
        editCourseButton.setOnClickListener(this);
        coursesButton = findViewById(R.id.courses_button);
        coursesButton.setOnClickListener(this);
        homeButton = findViewById(R.id.menu_home_button);
        homeButton.setOnClickListener(this);
        reportButton = findViewById(R.id.menu_report);
        messages = findViewById(R.id.messages);
        messages.setOnClickListener(this);
        reportButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if((mUser.getEmail().contains("@std.yildiz.edu.tr"))){
            editCourseButton.setVisibility(View.GONE);
            messages.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editCourse_Button && mUser.getEmail().contains("@yildiz.edu.tr")) {
            Intent intent = new Intent(getApplicationContext(), EditCourse.class);
            startActivity(intent);
        } else if (v.getId() == R.id.courses_button) {
            Intent intent = new Intent(Menu.this, CourseList.class);
            startActivity(intent);
        } else if (v.getId() == R.id.menu_home_button) {
            Intent intent = new Intent(Menu.this, Accounts_list.class);
            startActivity(intent);
        } else if (v.getId() == R.id.menu_report) {
            Intent intent = new Intent(Menu.this, Reporting.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.messages && mUser.getEmail().contains("@yildiz.edu.tr")) {
            Intent intent = new Intent(Menu.this, Messages.class);
            startActivity(intent);
        }

    }
}