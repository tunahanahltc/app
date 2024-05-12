package com.example.ytuobs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mCourseList;


    public CourseAdapter(Context context, ArrayList<String> courseList) {
        mContext = context;
        mCourseList = courseList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String courseName = mCourseList.get(position);
        holder.textCourseName.setText(courseName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(courseName);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String detailsText = "";
                        if (snapshot.exists()) {
                            String creatorInstructor = snapshot.child("Creator").getValue(String.class);
                            String date = snapshot.child("Date").getValue(String.class);
                            for (DataSnapshot grupSnapshot : snapshot.child("Gruplar").getChildren()) {
                                String grupName = grupSnapshot.getKey();
                                String grupInstructor = grupSnapshot.child("Akademisyen").getValue().toString();
                                detailsText += "\n"+"Grup ID : "+ grupName +"\n"+"EĞİTMEN : "+ grupInstructor + "\n";
                                detailsText+= grupName + " GRUBU ÖĞRENCİLERİ :" +"\n\n";
                                for(DataSnapshot students : grupSnapshot.child("Ogrenciler").getChildren()){
                                    detailsText += students.getValue().toString() + "\n";
                                }
                            }
                            Intent intent = new Intent(mContext,CourseProfile.class);
                            intent.putExtra("courseName",courseName);
                            intent.putExtra("courseCreator",creatorInstructor);
                            intent.putExtra("date",date);
                            intent.putExtra("details",detailsText);
                            mContext.startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Hata durumunda yapılacak işlemler
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCourseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourseName = itemView.findViewById(R.id.textCourseName);

        }
    }
}
