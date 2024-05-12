package com.example.ytuobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends Activity implements View.OnClickListener{

    Button login_button,register_botton;
    private EditText e_mail,student_no,name,password;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.e_mail = findViewById(R.id.e_mail_adress);
        this.password = findViewById(R.id.password);
        register_botton = findViewById(R.id.register_button);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        register_botton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.login_button){
            if(!TextUtils.isEmpty(e_mail.getText().toString())&& !TextUtils.isEmpty(password.getText().toString())){
                mAuth.signInWithEmailAndPassword(e_mail.getText().toString(),password.getText().toString())
                        .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                mUser = mAuth.getCurrentUser();

                                mReference = FirebaseDatabase.getInstance().getReference("Ogrenciler").child(mUser.getUid());
                                mReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Toast.makeText(Login.this,"Giris basarili",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Accounts_list.class);
                                        intent.putExtra("loginUserMail",mUser.getEmail());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }





        }
        else {
            Intent intent = new Intent(getApplicationContext(),Register.class);
            startActivity(intent);
        }


    }
}