package com.example.ytuobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Accounts_list extends AppCompatActivity implements View.OnClickListener {

        RecyclerView recyclerView;
        DatabaseReference databaseStudent,databaseInstruction;
        MyAdapter adapter;
        ArrayList<Member> list;
        ImageButton menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        recyclerView = findViewById(R.id.usersList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        String loginUserMail = (String) getIntent().getSerializableExtra("loginUserMail") ;
        adapter = new MyAdapter(this,list,loginUserMail);
        recyclerView.setAdapter(adapter);
        menu = findViewById(R.id.menuButton1);
        menu.setOnClickListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        list.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toList();

    }

    public void toList(){
        databaseStudent = FirebaseDatabase.getInstance().getReference("Ogrenciler");
        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    String name = snp.child("isim").getValue(String.class);
                    String eMail = snp.child("eMail").getValue(String.class);
                    String studentNo = snp.child("studentNo").getValue(String.class);
                    String educate = snp.child("educateInform").getValue(String.class);
                    String statu = snp.child("statu").getValue(String.class);
                    String telNo = snp.child("telNo").getValue(String.class);

                    Member member = new Student(eMail,name,studentNo);
                    member.setStatu(statu);
                    member.setEducateInform(educate);
                    member.setTel_no(telNo);
                    list.add(member);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Accounts_list.this,"Listelenme Yapilamadi",Toast.LENGTH_SHORT).show();
            }
        });

        databaseInstruction = FirebaseDatabase.getInstance().getReference("Akademisyenler");
        databaseInstruction.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    String name = snp.child("isim").getValue(String.class);
                    String eMail = snp.child("eMail").getValue(String.class);
                    String educate = snp.child("educateInform").getValue(String.class);
                    String statu = snp.child("statu").getValue(String.class);
                    String telNo = snp.child("telNo").getValue(String.class);

                    Member member = new Instructor(eMail,name);
                    member.setStatu(statu);
                    member.setEducateInform(educate);
                    member.setTel_no(telNo);
                    list.add(member);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Accounts_list.this,"Listelenme Yapilamadi",Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(),Menu.class);
        startActivity(intent);
    }
}