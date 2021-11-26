package com.amirmohammed.qrcodeattendance.ui;

import static com.amirmohammed.qrcodeattendance.ui.DoctorsLoginActivity.doctorId;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.adapters.CreatedLecturesAdapter;
import com.amirmohammed.qrcodeattendance.adapters.PreviousLecturesAdapter;
import com.amirmohammed.qrcodeattendance.databinding.ActivityDoctorsMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorsMainActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityDoctorsMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctors_main);

        getCreatedLectures();
    }

    private void getCreatedLectures() {

        firestore.collection("doctors").document(doctorId)
                .collection("lectures")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<String> previousLectures = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            String previousLectureId= snapshot.getString("lectureId");
                            previousLectures.add(previousLectureId);
                        }

                        CreatedLecturesAdapter previousLecturesAdapter =
                                new CreatedLecturesAdapter(previousLectures);
                        binding.rvPreviousCreatedLectures.setAdapter(previousLecturesAdapter);
                    }
                });
    }

    public void openCreateNewLecture(View view) {
        Intent intent = new Intent(DoctorsMainActivity.this, DoctorAddNewLectureActivity.class);
        startActivity(intent);
    }

}