package com.amirmohammed.qrcodeattendance.ui;

import static com.amirmohammed.qrcodeattendance.ui.StudentLoginActivity.studentId;
import static com.amirmohammed.qrcodeattendance.ui.StudentLoginActivity.studentName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amirmohammed.qrcodeattendance.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

public class ScanQrCodeActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CodeScanner mCodeScanner;
    private static final String TAG = "ScanQr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        Log.i(TAG, "onCreate: " + studentName);
        Log.i(TAG, "onCreate: " + studentId);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanQrCodeActivity.this, "Attending", Toast.LENGTH_SHORT).show();
                        attendToDoctor(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void attendToDoctor(String lectureId){
        String doctorId = lectureId.split(" ")[0];
        String lectureName = lectureId.split(" ")[1] + " " + lectureId.split(" ")[2];

        Map<String,Object> map = new HashMap<>();
        map.put("studentName", studentName);

        firestore.collection("doctors").document(doctorId)
                .collection("lectures").document(lectureName)
                .collection("attendance").document(studentName).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addToMyAttendance(lectureName);
                    }
                });
    }

    private void addToMyAttendance(String lectureName){
        Map<String,Object> map = new HashMap<>();
        map.put("lectureName", lectureName);

        firestore.collection("students").document(studentId)
                .collection("attendance").document(lectureName).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}