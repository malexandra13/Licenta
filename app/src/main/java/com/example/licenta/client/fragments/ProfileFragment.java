package com.example.licenta.client.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ProfileFragment extends Fragment {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView lastNameTextView;
    private TextView firstNameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private Button changePasswordButton;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_KEY = "MyPrefs";
    private static final String IMAGE_KEY = "profileImage";

    private ImageView profileImageView;
    private Uri imageUri;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("profile_images");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        lastNameTextView = view.findViewById(R.id.lastName);
        firstNameTextView = view.findViewById(R.id.firstName);
        emailTextView = view.findViewById(R.id.email);
        phoneNumberTextView = view.findViewById(R.id.phoneNumber);

        changePasswordButton = view.findViewById(R.id.buttonChangePassword);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        retrieveUserData(userId);

        profileImageView = view.findViewById(R.id.image);
        Button uploadButton = view.findViewById(R.id.buttonUploadImage);
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        loadProfileImage();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        return view;
    }

    private void loadProfileImage() {
        String imageUrl = sharedPreferences.getString(IMAGE_KEY, null);
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(profileImageView);
        }
    }

    private void retrieveUserData(String userId) {
        firestore.collection("clients")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String lastName = documentSnapshot.getString("lastName");
                            String firstName = documentSnapshot.getString("firstName");
                            String email = documentSnapshot.getString("email");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");

                            lastNameTextView.setText(lastName);
                            firstNameTextView.setText(firstName);
                            emailTextView.setText(email);
                            phoneNumberTextView.setText(phoneNumber);

                        }
                    }
                });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

        final EditText newPasswordEditText = dialogView.findViewById(R.id.password);
        final EditText confirmPasswordEditText = dialogView.findViewById(R.id.confirmPassword);

        builder.setView(dialogView)
                .setTitle("Change Password")
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = newPasswordEditText.getText().toString().trim();
                        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                            Toast.makeText(getActivity(), "Vă rugăm să introduceți atât noua parolă, cât și să confirmați parola", Toast.LENGTH_SHORT).show();
                        } else if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(getActivity(), "Parolele nu se potrivesc", Toast.LENGTH_SHORT).show();
                        } else {
                            mUser.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Parola a fost actualizată cu succes!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Nu s-a putut actualiza parola.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                uploadImageToStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToStorage(Bitmap bitmap) {
        String filename = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(IMAGE_KEY, imageUrl);
                        editor.apply();

                        Picasso.get().load(imageUrl).into(profileImageView);
                    }
                });
            }
        });
    }

}