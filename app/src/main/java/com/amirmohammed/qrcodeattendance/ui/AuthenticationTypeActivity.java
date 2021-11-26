package com.amirmohammed.qrcodeattendance.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amirmohammed.qrcodeattendance.R;

public class AuthenticationTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_type);

    }

    public void openLoginDoctors(View view) {
        Intent intent = new Intent(AuthenticationTypeActivity.this, DoctorsLoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void openLoginStudent(View view) {
        Intent intent = new Intent(AuthenticationTypeActivity.this, StudentLoginActivity.class);
        startActivity(intent);
        finish();
    }
}