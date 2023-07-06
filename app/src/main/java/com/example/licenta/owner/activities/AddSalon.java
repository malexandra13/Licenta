package com.example.licenta.owner.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.owner.MainOwnerActivity;
import com.example.licenta.owner.others.Salon;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    TextView salonName, salonStreet, salonPostalCode, salonPhone, salonEmail, salonDescription, salonCity;
    ImageView uploadImageView, salonImageView;
    Spinner salonStateSpinner;
    Button addSalonButton;
    RelativeLayout relativeLayout;
    Uri imageUri;

    private FirebaseDatabase database;
    private FirebaseStorage storage;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salon);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String accountId = currentUser.getUid();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Uploading salon");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        salonName = findViewById(R.id.salonName);
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

        salonStateSpinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_items_county));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        salonStateSpinner.setAdapter(adapter);



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
                progressDialog.show();
                final StorageReference reference = storage.getReference()
                        .child("salon")
                        .child(System.currentTimeMillis() + "");
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Salon salon = new Salon();
                                salon.setSalonImage(uri.toString());
                                salon.setSalonId(UUID.randomUUID().toString());
                                salon.setAccountId(currentUser.getUid());
                                salon.setSalonName(salonName.getText().toString());
                                salon.setSalonCity(salonCity.getText().toString());
                                salon.setSalonState(salonStateSpinner.getSelectedItem().toString());
                                salon.setSalonStreet(salonStreet.getText().toString());
                                salon.setSalonPostalCode(salonPostalCode.getText().toString());
                                salon.setSalonPhone(salonPhone.getText().toString());
                                salon.setSalonEmail(salonEmail.getText().toString());
                                salon.setSalonDescription(salonDescription.getText().toString());

                                database.getReference().child("salon").push()
                                        .setValue(salon).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddSalon.this, "Salon added successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(AddSalon.this, MainOwnerActivity.class);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddSalon.this, "Failed to add salon", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
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

