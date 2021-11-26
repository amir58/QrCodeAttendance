package com.amirmohammed.qrcodeattendance.ui;

import static com.amirmohammed.qrcodeattendance.ui.DoctorsLoginActivity.doctorId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.databinding.ActivityDoctorAddNewLectureBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorAddNewLectureActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<String> coursesNames = new ArrayList<>();
    String selectedCourseName = "";

    ActivityDoctorAddNewLectureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_add_new_lecture);

        getCourses();
        getSelectedCourse();
    }

    private void getCourses() {
        firestore.collection("courses")
                .get()
                .addOnSuccessListener(
                        new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    String courseName = snapshot.getString("name");
                                    coursesNames.add(courseName);
                                }

                                ArrayAdapter adapter =
                                        new ArrayAdapter(DoctorAddNewLectureActivity.this,
                                                android.R.layout.simple_list_item_1, coursesNames);

                                binding.courses.setAdapter(adapter);
                            }
                        }
                );
    }

    private void getSelectedCourse(){
        binding.courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCourseName = coursesNames.get(position);
            }
        });
    }

    public void createNewLecture(View view) {
        String lectureName = binding.etLectureName.getText().toString();

        if (lectureName.isEmpty()) {
            Toast.makeText(this, "lectureName required", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedCourseName.isEmpty()) {
            Toast.makeText(this, "course name required", Toast.LENGTH_LONG).show();
            return;
        }

        String lectureId = selectedCourseName + " " + lectureName;
        Map<String, Object> map = new HashMap<>();
        map.put("lectureId" , lectureId);

        firestore.collection("doctors").document(doctorId)
                .collection("lectures").document(lectureId)
                .set(map);

        Intent intent = new Intent(DoctorAddNewLectureActivity.this, DoctorLectureDetailsActivity.class);
        intent.putExtra("lectureId", lectureId);
        startActivity(intent);
        finish();
    }

}
