package com.amirmohammed.qrcodeattendance.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.databinding.ActivityDoctorsLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorsLoginActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityDoctorsLoginBinding binding;
    public static String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctors_login);

    }

    public void login(View view) {
        String id = binding.etDoctorId.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, "id required", Toast.LENGTH_LONG).show();
            return;
        }

        String password = binding.etDoctorPassword.getText().toString();

        if (password.isEmpty()) {
            Toast.makeText(this, "password required", Toast.LENGTH_LONG).show();
            return;
        }

        firestore.collection("doctors").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "Id not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String databaseId = documentSnapshot.get("id").toString();
                        String databasePassword = documentSnapshot.get("password").toString();
                        
                        if (id.equals(databaseId) && password.equals(databasePassword)){
                            doctorId = id;
                            Intent intent = new Intent(DoctorsLoginActivity.this, DoctorsMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
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