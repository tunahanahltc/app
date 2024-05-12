package com.example.ytuobs;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Messages extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String messages = "";
        TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        text = findViewById(R.id.inst_messages);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Akademisyenler");
        reference.child(mUser.getUid()).child("Mesajlar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 1;
                for(DataSnapshot snp : snapshot.getChildren()){
                    messages += counter +") " + snp.getValue().toString() + "\n";
                    counter +=1;
                }
                System.out.println(messages);
                text.setText(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Messages.this,"Hata",Toast.LENGTH_SHORT).show();
            }
        });


    }
}