package com.example.mentalhealthtracker.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mentalhealthtracker.databinding.FragmentCalendarBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private int currentRating;

    public void addEntry(int mood, String note, LocalDateTime timestamp) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> entry = new HashMap<>();
        entry.put("mood", mood);
        entry.put("note", note);
        entry.put("timestamp", timestamp);
        db.collection("userEntries")
                .add(entry)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error adding document" + e);
                });
    }

    // Create the view object
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calenderFeeling = binding.calendarFeeling; // Calendar
        RatingBar feeling = binding.feelingRating;              // Daily Star Rating
        EditText feelingBox = binding.textBox;                  // Text Box
        Button confirmButton = binding.calendarConfirmFeeling;  // Confirm button

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
                                + month + " / "
                                + year);
            }
        });

        // Daily Rating Change
        feeling.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    currentRating = (int)rating;
                    Log.d("FeelingRating", "User rated: " + rating);
                    addEntry(currentRating, "", LocalDateTime.now());
                }
            }
        });

        // Confirm Button listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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