package com.example.licenta.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.employee.login.LoginEmployee;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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

public class MainEmployeeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView greetingText;
    Button btnLogout;
    LinearLayout btnCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        greetingText = findViewById(R.id.greetingTextView);
        btnLogout = findViewById(R.id.buttonLogout);
        btnCalendar = findViewById(R.id.calendarEmployee);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEmployeeActivity.this, CalendarEmployeeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("employee").document(user.getUid());
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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainEmployeeActivity.this, gso);
                googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        Intent intent = new Intent(MainEmployeeActivity.this, LoginEmployee.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

    }
}