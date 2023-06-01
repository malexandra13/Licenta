package com.example.licenta.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.licenta.R;
import com.example.licenta.client.login_register.LoginClient;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainOwnerActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView greetingText;
    Button btnLogout;
    Button btnAddEmployee;

    Button btnAddSalon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnLogout = findViewById(R.id.buttonLogout);
        greetingText = findViewById(R.id.greetingTextView);
        btnAddEmployee = findViewById(R.id.buttonAddEmployee);
        btnAddSalon = findViewById(R.id.buttonAddSalon);

        btnAddSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOwnerActivity.this, AddSalon.class);
                startActivity(intent);
            }
        });

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOwnerActivity.this, AddEmployee.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainOwnerActivity.this, gso);
                googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        Intent intent = new Intent(MainOwnerActivity.this, LoginClient.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginClient.class);
            startActivity(intent);
            finish();
        }

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("owners").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                if (value != null && value.exists()) {
                    greetingText.setText("Hello, " + value.get("firstName") + " " + value.get("lastName") + "!");
                }
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            greetingText.setText("Hello, " + signInAccount.getDisplayName() + "!");

        } else {
            greetingText.setText("Hello, " + user.getDisplayName() + "!");
        }

    }

}