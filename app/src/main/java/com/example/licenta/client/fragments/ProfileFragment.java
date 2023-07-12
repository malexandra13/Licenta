package com.example.licenta.client.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    // Firestore instance
    private FirebaseFirestore firestore;
    private TextView lastNameTextView;
    private TextView firstNameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firestore = FirebaseFirestore.getInstance();

        lastNameTextView = view.findViewById(R.id.lastName);
        firstNameTextView = view.findViewById(R.id.firstName);
        emailTextView = view.findViewById(R.id.email);
        phoneNumberTextView = view.findViewById(R.id.phoneNumber);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();
        retrieveUserData(userId);
        Log.d("ProfileFragment", "onCreateView: " + userId);

        return view;
    }

    private void retrieveUserData(String userId) {
        firestore.collection("clients")
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // User document exists, retrieve data
                            String lastName = documentSnapshot.getString("lastName");
                            String firstName = documentSnapshot.getString("firstName");
                            String email = documentSnapshot.getString("email");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");

                            // Update TextViews with retrieved data
                            lastNameTextView.setText(lastName);
                            firstNameTextView.setText(firstName);
                            emailTextView.setText(email);
                            phoneNumberTextView.setText(phoneNumber);


                        }
                    }
                });
    }
}


