package com.example.mentalhealthtracker.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RatingBar feeling = binding.feelingRating;
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

        CalendarView calenderFeeling = binding.calendarFeeling;
        calenderFeeling.setDate(System.currentTimeMillis());
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}