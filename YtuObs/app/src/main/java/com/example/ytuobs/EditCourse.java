package com.example.ytuobs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class EditCourse extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference, databaseReference;
    private HashMap<String, Object> mData;
    private FirebaseUser mUser;
    String tmpUID;
    int flag;


    Button crtCourseButton, crtGrupButton, addStdButton, delStdButton;
    EditText courseId1, courseId2, groupId1, groupId2, date, studentMail, instMail, courseId3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = mAuth.getCurrentUser();
        crtCourseButton = findViewById(R.id.crtCourseButton);
        crtGrupButton = findViewById(R.id.crtGrupButton);
        addStdButton = findViewById(R.id.addStdButton);
        courseId2 = findViewById(R.id.courseId2);
        groupId1 = findViewById(R.id.groupId1);
        groupId2 = findViewById(R.id.groupId2);
        courseId3 = findViewById(R.id.courseId3);
        studentMail = findViewById(R.id.studentMail);
        instMail = findViewById(R.id.instMail);
        delStdButton = findViewById(R.id.delStdButton);
        courseId1 = findViewById(R.id.courseId1);
        date = findViewById(R.id.date_editxt);
        crtCourseButton.setOnClickListener(this);
        crtGrupButton.setOnClickListener(this);
        addStdButton.setOnClickListener(this);
        delStdButton.setOnClickListener(this);
        setFlag(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crtCourseButton:
                create_course();
                     break;

            case R.id.crtGrupButton:
                create_group();
                    break;

            case R.id.addStdButton:
                addStudentToGroup(courseId3.getText().toString(),groupId2.getText().toString(),studentMail.getText().toString());
                    break;

            case R.id.delStdButton:
                removeStudentFromGroup(courseId3.getText().toString(),groupId2.getText().toString(),studentMail.getText().toString());
                    break;

            default:
                break;
        }
    }


    public void create_course() {
        mData = new HashMap<>();
        mData.put("CourseId", courseId1.getText().toString());
        mData.put("Date", date.getText().toString());
        mData.put("Creator", mUser.getEmail());

        mReference.child("Courses").child(courseId1.getText().toString())
                .setValue(mData)
                .addOnCompleteListener(EditCourse.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditCourse.this, "Kurs başarılı bir şekilde oluşturuldu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditCourse.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void create_group() {

        mData = new HashMap<>();


        mReference.child("Courses").child(courseId2.getText().toString())
                .child("Gruplar").child(groupId1.getText().toString())
                .child("Akademisyen").setValue(instMail.getText().toString())
                .addOnCompleteListener(EditCourse.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditCourse.this, "Grup basarili bir sekilde olusturuldu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditCourse.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void addStudentToGroup(String courseId, String groupId, String stdMail) {
        DatabaseReference databaseReferenceStudents = FirebaseDatabase.getInstance().getReference("Ogrenciler");
        databaseReferenceStudents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String studentEmail = studentSnapshot.child("eMail").getValue(String.class);
                    if (studentEmail != null && studentEmail.equals(stdMail)) {
                        String studentKey = studentSnapshot.getKey();
                        DatabaseReference databaseReferenceCourses = FirebaseDatabase.getInstance().getReference("Courses");
                        databaseReferenceCourses.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(courseId)) {
                                    DataSnapshot courseSnapshot = dataSnapshot.child(courseId);
                                    if (courseSnapshot.child("Gruplar").hasChild(groupId)) {
                                        DataSnapshot groupSnapshot = courseSnapshot.child("Gruplar").child(groupId);
                                        if (groupSnapshot.child("Ogrenciler").hasChild(studentKey)) {

                                            showToast("Öğrenci zaten bu gruba kayıtlı");
                                        } else {
                                            DatabaseReference groupStudentsRef = groupSnapshot.child("Ogrenciler").getRef();
                                            groupStudentsRef.child(studentKey).setValue(stdMail).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    showToast("Öğrenci başarıyla kaydedildi");
                                                } else {
                                                    showToast("Öğrenci kaydedilirken bir hata oluştu");
                                                }
                                            });
                                        }
                                    } else {
                                        showToast("Grup bulunamadı");
                                    }
                                } else {
                                    showToast("Ders bulunamadı");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                showToast("Hata oluştu: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Hata oluştu: " + error.getMessage());
            }
        });
    }


    public void removeStudentFromGroup(String courseId, String groupId, String stdMail) {
        DatabaseReference groupStudentsRef = FirebaseDatabase.getInstance().getReference("Courses")
                .child(courseId).child("Gruplar").child(groupId).child("Ogrenciler");

        groupStudentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean studentFound = false;
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String studentEmail = studentSnapshot.getValue(String.class);
                    if (studentEmail != null && studentEmail.equals(stdMail)) {
                        studentSnapshot.getRef().removeValue();
                        studentFound = true;
                        break;
                    }
                }
                if (studentFound) {
                    showToast("Öğrenci başarıyla gruptan silindi");
                } else {
                    showToast("Öğrenci bulunamadı");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Hata oluştu: " + error.getMessage());
            }
        });
    }





    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(EditCourse.this, message, Toast.LENGTH_SHORT).show());
    }


    public void setTmpUID(String tmpUID) {
        this.tmpUID = tmpUID;
    }

    public String getTmpUID() {
        return tmpUID;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}