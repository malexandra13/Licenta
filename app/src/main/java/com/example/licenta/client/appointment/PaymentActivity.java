package com.example.licenta.client.appointment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.licenta.R;
import com.example.licenta.client.MainClientActivity;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    Button payment, btnHome;
    String PublishableKey = "pk_test_51NNL8sCIYzdrqpkOUjsotNkMO0Mi5L64XAbNjTXYF4SVUt9clo0Y7kiJwXkjdM340Xy78t9ssmxLSSHxZK01VmGC000VbOfVaN";
    String SecretKey = "sk_test_51NNL8sCIYzdrqpkONMfonFNuJKjQDx00MccQWmnXIHpafMETngc8GJpjTcbGuWvRMWiFrFWPaHclw9w3TGZHDSQa00MykjoHjX";
    String CostumerId;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;
    ProgressBar progressBar;
    TextView tvSalonName, tvServiceName, tvServicePrice, tvServiceDate, tvServiceTime, tvEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvSalonName = findViewById(R.id.salonName);
        tvServiceName = findViewById(R.id.serviceName);
        tvServicePrice = findViewById(R.id.price);
        tvServiceDate = findViewById(R.id.date);
        tvServiceTime = findViewById(R.id.hour);
        tvEmployee = findViewById(R.id.employeeName);

        tvSalonName.setText(getIntent().getStringExtra("salonName"));
        tvServiceName.setText(getIntent().getStringExtra("serviceName"));
        tvServicePrice.setText(getIntent().getStringExtra("price") + " RON");
        tvServiceDate.setText(getIntent().getStringExtra("date"));
        tvServiceTime.setText(getIntent().getStringExtra("time"));
        tvEmployee.setText(getIntent().getStringExtra("employeeName"));

        progressBar = findViewById(R.id.progressBar);


        payment = findViewById(R.id.btn_pay);
        PaymentConfiguration.init(this, PublishableKey);

        paymentSheet = new PaymentSheet(this, result -> {
            if (result instanceof PaymentSheetResult.Canceled) {
                Toast.makeText(this, "Plată anulată", Toast.LENGTH_SHORT).show();
            } else if (result instanceof PaymentSheetResult.Failed) {
                Toast.makeText(this, "Plată eșuată", Toast.LENGTH_SHORT).show();
            } else if (result instanceof PaymentSheetResult.Completed) {
                Toast.makeText(this, "Plată finalizată", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, SuccessfulPaymentActivity.class);
                startActivity(intent);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                paymentSheet.presentWithPaymentIntent(
                        ClientSecret,
                        new PaymentSheet.Configuration(
                                "Beauty Salon",
                                new PaymentSheet.CustomerConfiguration(
                                        CostumerId,
                                        EphericalKey
                                )
                        )
                );
            }
        });

        btnHome = findViewById(R.id.salonPay);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, MainClientActivity.class);
                startActivity(intent);
            }
        });

        progressBar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            CostumerId = jsonObject.getString("id");
                            getEmphericalKey();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            //error
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getEmphericalKey() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            EphericalKey = jsonObject.getString("id");
                            getClientSecret(CostumerId, EphericalKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CostumerId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String costumerId, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ClientSecret = jsonObject.getString("client_secret");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            //error
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PaymentActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error", error.toString());
            }

        }) {
            //header
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CostumerId);
                params.put("amount", getIntent().getStringExtra("price") + "00");
                params.put("currency", "RON");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}



