package com.amirmohammed.qrcodeattendance.ui;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import static com.amirmohammed.qrcodeattendance.ui.DoctorsLoginActivity.doctorId;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.adapters.PreviousLecturesAdapter;
import com.amirmohammed.qrcodeattendance.databinding.ActivityDoctorLectureDetailsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DoctorLectureDetailsActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ActivityDoctorLectureDetailsBinding binding;
    String lectureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_lecture_details);

         lectureId = getIntent().getStringExtra("lectureId");

        try {
            String qrCodeKey = doctorId + " " + lectureId;
            binding.qrCode.setImageBitmap(generateQrCode(qrCodeKey));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        getLiveAttendance();
    }

    private void getLiveAttendance() {
        firestore.collection("doctors").document(doctorId)
                .collection("lectures").document(lectureId)
                .collection("attendance")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<String> previousLectures = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            String previousLectureId= snapshot.getString("studentName");
                            previousLectures.add(previousLectureId);
                        }
                        binding.tvTotalStudentsCount.setText("Students count : " + previousLectures.size());

                        PreviousLecturesAdapter previousLecturesAdapter = new PreviousLecturesAdapter(previousLectures);
                        binding.rvStudentsAttendance.setAdapter(previousLecturesAdapter);
                    }
                });
    }

    public static Bitmap generateQrCode(String myCodeText) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% damage

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int size = 256;

        BitMatrix bitMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

}
