package com.example.mentalhealthtracker;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mentalhealthtracker.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.type.DateTime;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chat, R.id.navigation_calendar, R.id.navigation_help)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_container);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(binding.navView, navController);
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // create a new entry in the database, the datatypes will be integer, string, and timestamp
        Map<Number, LocalDateTime> entry = new HashMap<>();
        entry.put(5, LocalDateTime.now());
        entry.put(10, LocalDateTime.now());


        db.collection("entries")
                .add(entry)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error adding document" + e);
                });

        db.collection("entries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                            System.out.println(document.getId() + " => " + document.getId());
                        }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error getting documents" + e);
                });

        NavigationUI.setupWithNavController(navView, navController);
    }

}