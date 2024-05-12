package com.example.ytuobs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Reporting extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button button;
    Spinner spinner1;
    EditText courseName;
    EditText insMail;
    TextView text;
    TextView coursnametxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);
        button = findViewById(R.id.send_button);
        button.setOnClickListener(this);
        spinner1 = findViewById(R.id.spinner1);
        courseName = findViewById(R.id.reporting_course_name);
        insMail = findViewById(R.id.reporting_instMail);
        text = findViewById(R.id.report_message);
        coursnametxt = findViewById(R.id.courseNametxt);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        ArrayList<String> optionsList = new ArrayList<>();
        optionsList.add("Uygulama");
        optionsList.add("Kurs");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Akademisyenler");

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = optionsList.get(position);

                if (selectedOption == "Uygulama") {
                    courseName.setVisibility(View.GONE);
                    coursnametxt.setVisibility(View.GONE);


                } else if (selectedOption == "Kurs") {
                    courseName.setVisibility(View.VISIBLE);
                    coursnametxt.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        String selectedOption = spinner1.getSelectedItem().toString();

        int selectedPosition = spinner1.getSelectedItemPosition();

        if (selectedOption.equals("Uygulama")) {
            application_message();
        } else if (selectedOption.equals("Kurs")) {
            course_message();
        }

    }


    public void application_message() {
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for (DataSnapshot snp : snapshot.getChildren()) {
                    if (snp.child("eMail").getValue().toString().equals(insMail.getText().toString())) {
                        flag = 1;
                        DatabaseReference reference = snp.child("Mesajlar").getRef();
                        reference.child(mUser.getUid()).setValue(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Reporting.this, "Mesaj gonderildi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                if (flag == 0) {
                    Toast.makeText(Reporting.this, "Akademisyen bulumadi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Reporting.this, "HATA", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void course_message() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        databaseReference.child(courseName.getText().toString()).child("Mesajlar").child(mUser.getUid())
                .setValue(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for (DataSnapshot snp : snapshot.getChildren()) {
                    if (snp.child("eMail").getValue().toString().equals(insMail.getText().toString())) {
                        flag = 1;
                        DatabaseReference reference = snp.child("Mesajlar").getRef();
                        reference.child(mUser.getUid()).setValue(text.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Reporting.this, "Mesaj gonderildi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                if (flag == 0) {
                    Toast.makeText(Reporting.this, "Akademisyen bulumadi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Reporting.this, "HATA", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
