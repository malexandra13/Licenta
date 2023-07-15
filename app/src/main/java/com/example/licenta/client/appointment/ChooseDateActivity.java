package com.example.licenta.client.appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.client.others.Appointment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChooseDateActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference appointmentsRef;

    private DatePicker datePicker;
    private GridLayout hourSlotsLayout;
    private Button bookButton;

    private List<Button> hourSlotButtons;
    private Button selectedHourSlotButton = null;

    private static final int SERVICE_DURATION = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        appointmentsRef = firebaseDatabase.getReference("appointments");

        datePicker = findViewById(R.id.datePicker);
        hourSlotsLayout = findViewById(R.id.hourSlotsLayout);
        bookButton = findViewById(R.id.bookButton);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment(selectedHourSlotButton);
            }
        });

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                hourSlotsLayout.removeAllViews();
                hourSlotButtons.clear();
                generateAvailableHourSlots();
            }
        });

        hourSlotButtons = new ArrayList<>();
        generateAvailableHourSlots();
    }

    private void generateAvailableHourSlots() {
        int startHour = Integer.parseInt(getIntent().getStringExtra("startHour"));
        int endHour = Integer.parseInt(getIntent().getStringExtra("endHour"));

        int totalWorkingHours = endHour - startHour;
        int availableSlots = totalWorkingHours * 60 / SERVICE_DURATION;

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

        List<Calendar> availableSlotsList = new ArrayList<>();
        for (int i = 0; i < availableSlots; i++) {
            Calendar slot = (Calendar) selectedDate.clone();
            slot.set(Calendar.HOUR_OF_DAY, startHour + (i * SERVICE_DURATION / 60));
            slot.set(Calendar.MINUTE, (i * SERVICE_DURATION) % 60);
            availableSlotsList.add(slot);
        }
        Calendar currentDate = Calendar.getInstance();

        if (selectedDate.before(currentDate)) {
            for (Button button : hourSlotButtons) {
                button.setEnabled(false);
            }
            return;
        }

        int columnCount = 3; // Numărul de coloane pentru GridLayout
        int rowCount = (int) Math.ceil((double) availableSlotsList.size() / columnCount); // Calculează numărul de rânduri

        hourSlotsLayout.setColumnCount(columnCount);
        hourSlotsLayout.setRowCount(rowCount);

        for (Calendar slot : availableSlotsList) {
            Button button = createHourSlotButton(slot);
            hourSlotButtons.add(button);
            hourSlotsLayout.addView(button);
        }

        String selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);

                    if (appointment.getDate().equals(selectedDateString)) {
                        Calendar bookedSlot = Calendar.getInstance();

                        try {
                            Date appointmentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(appointment.getDate() + " " + appointment.getTime());
                            bookedSlot.setTime(appointmentDate);

                            for (Button button : hourSlotButtons) {
                                Calendar slot = (Calendar) button.getTag();
                                if (isSameHour(slot, bookedSlot)) {
                                    button.setEnabled(false);
                                    break;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error here
            }
        });
    }

    private Button createHourSlotButton(Calendar slot) {
        Button button = new Button(this);
        button.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));
        button.setText(formatTime(slot.getTime()));
        button.setTag(slot);

        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.set(Calendar.SECOND, 0);
        currentDateTime.set(Calendar.MILLISECOND, 0);

        if (slot.before(currentDateTime)) {
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                if (clickedButton.isEnabled()) {
                    if (selectedHourSlotButton != null) {
                        selectedHourSlotButton.setEnabled(true);
                    }
                    clickedButton.setEnabled(false);
                    selectedHourSlotButton = clickedButton;
                    bookButton.setEnabled(true);
                } else {
                    Toast.makeText(ChooseDateActivity.this, "This hour slot is already booked.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return button;
    }

    private void bookAppointment(Button selectedButton) {
        try {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            String hourSlot = selectedButton.getText().toString();
            String salondId = getIntent().getStringExtra("salonId");
            String serviceId = getIntent().getStringExtra("serviceId");
            String price = getIntent().getStringExtra("price");
            String employeeId = getIntent().getStringExtra("employeeId");

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String accountId = currentUser.getUid();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, Integer.parseInt(hourSlot.substring(0, 2)), 0);

            Calendar currentDateTime = Calendar.getInstance();
            if (calendar.before(currentDateTime)) {
                Toast.makeText(ChooseDateActivity.this, "Cannot book appointment for past time.", Toast.LENGTH_SHORT).show();
                return;
            }

            String formattedTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());

            Appointment appointment = new Appointment(salondId, accountId, employeeId, serviceId, price, formattedDate, formattedTime);

            DatabaseReference appointmentsRef = firebaseDatabase.getReference().child("appointments");
            String appointmentId = appointmentsRef.push().getKey();

            Log.d("alexandra", appointment.toString());

            appointmentsRef.child(appointmentId).setValue(appointment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(ChooseDateActivity.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                            selectedButton.setEnabled(false);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ChooseDateActivity.this, PaymentActivity.class);
                                    intent.putExtra("price", price);
                                    intent.putExtra("serviceId", serviceId);
                                    intent.putExtra("salonId", salondId);
                                    intent.putExtra("date", formattedDate);
                                    intent.putExtra("time", formattedTime);
                                    intent.putExtra("employeeName", getIntent().getStringExtra("employeeName"));
                                    intent.putExtra("serviceName", getIntent().getStringExtra("serviceName"));
                                    intent.putExtra("salonName", getIntent().getStringExtra("salonName"));
                                    startActivity(intent);

                                }
                            }, 2000);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChooseDateActivity.this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSameHour(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
                && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE);
    }

    private String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date).toLowerCase();
    }
}
