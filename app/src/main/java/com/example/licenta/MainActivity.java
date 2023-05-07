package com.example.licenta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity extends AppCompatActivity {

    TextView navHeaderName;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        NavigationView navigationView = findViewById(R.id.navigationView);

        View headerView = navigationView.getHeaderView(0);
        navHeaderName = headerView.findViewById(R.id.navHeaderName);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("users").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                if (value != null && value.exists()) {
                    navHeaderName.setText("Hello, " + value.get("firstName") + " " + value.get("lastName") + "!");
                }
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            navHeaderName.setText("Hello, " + signInAccount.getDisplayName() + "!");

        }

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageViewMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        textViewTitle = findViewById(R.id.textViewAppName);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@androidx.annotation.NonNull NavController navController, @androidx.annotation.NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.nav_home) {
                    textViewTitle.setText("Home");
                } else if (navDestination.getId() == R.id.nav_account) {
                    textViewTitle.setText("Profile");
                } else if (navDestination.getId() == R.id.nav_inbox) {
                    textViewTitle.setText("Inbox");
                } else if (navDestination.getId() == R.id.nav_about) {
                    textViewTitle.setText("About");
                } else if (navDestination.getId() == R.id.nav_settings) {
                    textViewTitle.setText("Settings");
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navHostFragment, new HomeFragment())
                            .commit();
                } else if (itemId == R.id.nav_account) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navHostFragment, new ProfileFragment())
                            .commit();
                } else if (itemId == R.id.nav_inbox) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navHostFragment, new InboxFragment())
                            .commit();
                } else if (itemId == R.id.nav_about) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navHostFragment, new AboutFragment())
                            .commit();
                } else if (itemId == R.id.nav_settings) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navHostFragment, new SettingsFragment())
                            .commit();
                } else if (itemId == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }

                DrawerLayout drawer = findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });




    }



}