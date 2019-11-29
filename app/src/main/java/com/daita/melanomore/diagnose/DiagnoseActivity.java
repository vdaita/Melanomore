package com.daita.melanomore.diagnose;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.daita.melanomore.R;
import com.daita.melanomore.data.Diagnosis;
import com.daita.melanomore.main.MainActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DiagnoseActivity extends AppCompatActivity {

    DiagnoseViewModel mViewModel;
    EditText mNotesEditText;
    ImageView mImageView;
    TextView mDiagnosisTextView;
    TextView mAlternativeDiagnosisTextView;
    FloatingActionButton mFab;
    String mImageFileName;

    public static final String TAG = "DiagnoseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNotesEditText = findViewById(R.id.diagnoseNotes);
        mImageView = findViewById(R.id.diagnoseImageView);
        mDiagnosisTextView = findViewById(R.id.diagnosisTextView);
        mAlternativeDiagnosisTextView = findViewById(R.id.diagnosisAlternativeTextView);
        mFab = findViewById(R.id.fab);

        mViewModel = ViewModelProviders.of(this).get(DiagnoseViewModel.class);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(DiagnoseActivity.this)
                    .crop(1f, 1f)
                    .compress(1024)
                    .maxResultSize(800, 800)
                    .start();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diagnosisString = mDiagnosisTextView.getText().toString();
                String diagnosisNotes = mNotesEditText.getText().toString();
                String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                Diagnosis diagnosis = new Diagnosis(diagnosisString, mImageFileName, timeStamp, diagnosisNotes);
                mViewModel.getRepository().insertDiagnosis(diagnosis);

                Intent intent = new Intent(DiagnoseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            mImageView.setImageBitmap(bitmap);

            mImageFileName = data.getData().toString();

            // Remote Model
//            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
//                    .requireWifi()
//                    .build();
//            FirebaseRemoteModel remoteModel = new FirebaseRemoteModel.Builder("ISIC2019_cloud")
//                    .enableModelUpdates(true)
//                    .setInitialDownloadConditions(conditions)
//                    .setUpdatesDownloadConditions(conditions)
//                    .build();
//            FirebaseModelManager.getInstance().registerRemoteModel(remoteModel);


            // Local Model
            FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("isic2019_model")
                    .setAssetFilePath("manifest.json")
                    .build();
            FirebaseModelManager.getInstance().registerLocalModel(localModel);

            // Creating the image
            final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

            // Labeler
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("isic2019_model")
//                        .setRemoteModelName("ISIC2019_cloud")
                        .setConfidenceThreshold(0f)
                        .build();
            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);

            processImage(labeler, image);

        } catch (FirebaseMLException e) {
            mDiagnosisTextView.setText("error");
            e.printStackTrace();
        } catch (IOException e){
            mDiagnosisTextView.setText("error");
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processImage(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image){
        labeler.processImage(image).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDiagnosisTextView.setText("error");
            }
        }).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {

                Log.v(TAG, "Firebase Vision Labels: " + firebaseVisionImageLabels.toString());

                // Primary diagnosis
                mDiagnosisTextView.setText(firebaseVisionImageLabels.get(0).getText());

                // Alternative predictions
                StringBuilder stringBuilder = new StringBuilder();
                for (FirebaseVisionImageLabel label: firebaseVisionImageLabels){
                    stringBuilder.append(label.getText() + ": " + label.getConfidence() * 100 + "%" + "\n");
                }
                mAlternativeDiagnosisTextView.setText(stringBuilder.toString());
            }
        });
    }
}
