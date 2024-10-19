package com.example.mentalhealthtracker.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mentalhealthtracker.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private LocalDateTime currentDate;



    public void getEntriesForDate(LocalDateTime date) {
        System.out.println("Getting entries for date: " + date);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userEntries")
                .whereEqualTo("timestamp", date)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (Map.Entry<String, Object> entry : task.getResult().toObjects(Map.Entry.class)) {
                            Log.d("CalendarFragment", "Entry: " + entry.getKey() + " => " + entry.getValue());
                        }
                    } else {
                        Log.d("CalendarFragment", "Error getting documents: ", task.getException());
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calenderFeeling = binding.calendarFeeling; // Calendar
        RatingBar feeling = binding.feelingRating;              // Daily Star Rating
        TextView feelingBox = binding.textRecord;               // Text Box

        // Setup page
        calenderFeeling.setDate(System.currentTimeMillis());

        // Start listeners

        // Calender Date Change
        calenderFeeling.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("Feeling-Calendar",
                        "User set the date to: "
                                + dayOfMonth + " / "
                                + (month+1) + " / "
                                + year);
                currentDate = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0);
                feelingBox.setText("You have not recorded any feelings for this day.");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}