package com.example.licenta.owner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class AddSalon extends AppCompatActivity {

    TextView salonName, salonState, salonCity, salonStreet, salonPostalCode, salonPhone, salonEmail, salonDescription;
    ImageView uploadImageView, salonImageView;
    Button addSalonButton;
    RelativeLayout relativeLayout;
    Uri imageUri;

    private FirebaseDatabase database;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        salonName = findViewById(R.id.salonName);
        salonState = findViewById(R.id.salonState);
        salonCity = findViewById(R.id.salonCity);
        salonStreet = findViewById(R.id.salonStreet);
        salonPostalCode = findViewById(R.id.salonPostalCode);
        salonPhone = findViewById(R.id.phoneNumber);
        salonEmail = findViewById(R.id.salonEmail);
        salonDescription = findViewById(R.id.salonDescription);
        uploadImageView = findViewById(R.id.uploadImage);
        salonImageView = findViewById(R.id.salonImage);
        relativeLayout = findViewById(R.id.relative);
        addSalonButton = findViewById(R.id.addSalon);

        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
                relativeLayout.setVisibility(View.VISIBLE);
                uploadImageView.setVisibility(View.GONE);

            }
        });

        addSalonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final StorageReference reference = storage.getReference()
                        .child("salon")
                        .child(System.currentTimeMillis() + "");

                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                ProjectModel projectModel = new ProjectModel();
                                projectModel.setSalonImage(uri.toString());

                                projectModel.setSalonName(salonName.getText().toString());
                                projectModel.setSalonState(salonState.getText().toString());
                                projectModel.setSalonCity(salonCity.getText().toString());
                                projectModel.setSalonStreet(salonStreet.getText().toString());
                                projectModel.setSalonPostalCode(salonPostalCode.getText().toString());
                                projectModel.setSalonPhone(salonPhone.getText().toString());
                                projectModel.setSalonEmail(salonEmail.getText().toString());
                                projectModel.setSalonDescription(salonDescription.getText().toString());

                                database.getReference().child("salon").push()
                                        .setValue(projectModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddSalon.this, "Salon added successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AddSalon.this, MainOwnerActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(AddSalon.this, "Failed to add salon", Toast.LENGTH_LONG).show();

                                            }
                                        });

                            }
                        });
                    }
                });
            }
        });

    }

    private void uploadPicture() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 101);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddSalon.this, "Permission denied", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            salonImageView.setImageURI(imageUri);
        }

    }
}

