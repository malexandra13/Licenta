package com.example.licenta.employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarEmployeeActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_employee);

        db = FirebaseFirestore.getInstance();

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            fetchAppointmentsForDate(selectedDate);
        });
    }


    private void getEmployeeIdForCurrentUser(OnEmployeeIdReceivedListener listener) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("employee")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String employeeId = document.getString("employeeId");
                            listener.onEmployeeIdReceived(employeeId);
                        } else {
                            listener.onError("Documentul nu există");
                        }
                    } else {
                        listener.onError("Eroare la preluarea datelor: " + task.getException().getMessage());
                    }
                });
    }

    private void fetchAppointmentsForDate(Calendar selectedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        getEmployeeIdForCurrentUser(new OnEmployeeIdReceivedListener() {
            @Override
            public void onEmployeeIdReceived(String employeeId) {
                String employeeIdForCurrentUser = employeeId;

                FirebaseDatabase.getInstance().getReference("appointments")
                        .orderByChild("date").equalTo(formattedDate)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                LinearLayout llAppointments = findViewById(R.id.llAppointments);
                                llAppointments.removeAllViews();

                                boolean hasAppointments = false;

                                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                                    String time = appointmentSnapshot.child("time").getValue(String.class);
                                    String price = appointmentSnapshot.child("price").getValue(String.class);
                                    String employeeIdFromDB = appointmentSnapshot.child("employeeId").getValue(String.class);

                                    if (employeeIdFromDB.equals(employeeIdForCurrentUser)) {
                                        hasAppointments = true;
                                        String appointmentText = "Time: " + time + ", Price: " + price;
                                        TextView textView = new TextView(CalendarEmployeeActivity.this);
                                        textView.setText(appointmentText);
                                        textView.setTextSize(18);
                                        textView.setTextColor(getResources().getColor(R.color.black));
                                        llAppointments.addView(textView);
                                    }
                                }

                                if (!hasAppointments) {
                                    TextView textView = new TextView(CalendarEmployeeActivity.this);
                                    textView.setText(getResources().getText(R.string.no_appointments));
                                    textView.setTextSize(18);
                                    textView.setTextColor(getResources().getColor(R.color.black));
                                    llAppointments.addView(textView);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }


            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CalendarEmployeeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    interface OnEmployeeIdReceivedListener {
        void onEmployeeIdReceived(String employeeId);

        void onError(String errorMessage);
    }
}
