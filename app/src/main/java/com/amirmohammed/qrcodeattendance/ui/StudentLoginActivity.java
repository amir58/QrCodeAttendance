package com.amirmohammed.qrcodeattendance.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.databinding.ActivityStudentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentLoginActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityStudentLoginBinding binding;
    public static String studentName;
    public static String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_login);

    }

    public void login(View view) {
        String id = binding.etStudentId.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, "id required", Toast.LENGTH_LONG).show();
            return;
        }

        String password = binding.etStudentPassword.getText().toString();

        if (password.isEmpty()) {
            Toast.makeText(this, "password required", Toast.LENGTH_LONG).show();
            return;
        }

        firestore.collection("students").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Id not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String databaseId = documentSnapshot.getString("id");
                        String databasePassword = documentSnapshot.getString("password");                            studentName = documentSnapshot.getString("name");

                        if (id.equals(databaseId) && password.equals(databasePassword)) {
                            studentId = documentSnapshot.getString("id");
                            studentName = documentSnapshot.getString("name");

                            Intent intent = new Intent(StudentLoginActivity.this, StudentMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Id or password were wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getLocalizedMessage();
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


    }


}