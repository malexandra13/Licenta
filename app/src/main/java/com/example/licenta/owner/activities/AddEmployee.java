package com.example.licenta.owner.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.licenta.R;
import com.example.licenta.owner.others.Employee;
import com.example.licenta.owner.others.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEmployee extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextLastName, editTextFirstName, editTextPhoneNumber;
    Button buttonRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;
    Spinner spinnerDepartment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonRegister = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        editTextLastName = findViewById(R.id.lastName);
        editTextFirstName = findViewById(R.id.firstName);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        spinnerDepartment = findViewById(R.id.spinner);
        String salonId = getIntent().getStringExtra("salonId");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_items_department));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDepartment.setAdapter(adapter);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String userType = "employee";
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                String lastName = String.valueOf(editTextLastName.getText());
                String firstName = String.valueOf(editTextFirstName.getText());
                String phoneNumber = String.valueOf(editTextPhoneNumber.getText());
                String department = spinnerDepartment.getSelectedItem().toString();
                String ownerId = mAuth.getCurrentUser().getUid();

                if (TextUtils.isEmpty(firstName)) {
                    editTextFirstName.setError("First name is required");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    editTextLastName.setError("Last name is required");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    editTextPhoneNumber.setError("Phone number is required");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email is required");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters");
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddEmployee.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    firebaseFirestore.collection("employee").
                                            document(mAuth.getCurrentUser().getUid()).
                                            set(new Employee(userType, ownerId, firstName, lastName, phoneNumber, email, salonId, department));
                                } else {
                                    Toast.makeText(AddEmployee.this, "Account already exist.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}



