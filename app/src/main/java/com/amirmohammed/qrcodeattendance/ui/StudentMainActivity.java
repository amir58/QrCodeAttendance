package com.amirmohammed.qrcodeattendance.ui;

import static com.amirmohammed.qrcodeattendance.ui.StudentLoginActivity.studentId;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.adapters.PreviousLecturesAdapter;
import com.amirmohammed.qrcodeattendance.databinding.ActivityStudentMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StudentMainActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityStudentMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_main);

        getPreviousLectures();
    }

    private void getPreviousLectures() {
        firestore.collection("students").document(studentId)
                .collection("attendance")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<String> previousLectures = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            String previousLectureId = snapshot.getString("lectureName");
                            previousLectures.add(previousLectureId);
                        }

                        PreviousLecturesAdapter previousLecturesAdapter = new PreviousLecturesAdapter(previousLectures);
                        binding.rvPreviousAttendedLectures.setAdapter(previousLecturesAdapter);
                    }
                });

    }

    ActivityResultLauncher<String[]> permissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean cameraGranted = result.getOrDefault(Manifest.permission.CAMERA, false);

                        if (cameraGranted != null && cameraGranted) {
                            // Precise camera access granted.
                            Intent intent = new Intent(StudentMainActivity.this, ScanQrCodeActivity.class);
                            startActivity(intent);
                        } else {
                            // No camera access granted.
                            Toast.makeText(StudentMainActivity.this, "You can't scan QR Code with out camera", Toast.LENGTH_LONG).show();
                        }
                    }
            );

    public void scanToAttend(View view) {
        permissionRequest.launch(new String[]{Manifest.permission.CAMERA,});
    }

}
