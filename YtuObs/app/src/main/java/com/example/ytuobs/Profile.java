package com.example.ytuobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    EditText name,telNo,educate,mail,studentId;
    TextView statu;
    Button saveButton,editButton;
    ImageButton menu_button;
    Member loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editButton = findViewById(R.id.editButton);

        statu = findViewById(R.id.statu);
        name = findViewById(R.id.profile_name);
        telNo = findViewById(R.id.profile_phone);
        educate = findViewById(R.id.profile_educate);
        mail = findViewById(R.id.profile_mail);
        studentId = findViewById(R.id.profile_studentId);
        menu_button = findViewById(R.id.menu_button_profile);
        menu_button.setOnClickListener(this);

        telNo.setEnabled(false);
        educate.setEnabled(false);
        loginUser = (Member) getIntent().getSerializableExtra("currentUser");
        setInformationProfile();

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInformationProfile();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getApplicationContext(),Menu.class);
        startActivity(intent);
        }

    public void setInformationProfile(){
        statu.setText(loginUser.getStatu());
        educate.setText(loginUser.getEducateInform());
        mail.setText(loginUser.getE_mail());
        telNo.setText(loginUser.getTel_no());
        name.setText(loginUser.getName());

        if(mail.getText().toString().contains("@std.yildiz.edu.tr"))
        {
            Student student =(Student) loginUser;
            studentId.setText(student.getStudent_no());
            studentId.setVisibility(View.VISIBLE);
        }



    }
}
