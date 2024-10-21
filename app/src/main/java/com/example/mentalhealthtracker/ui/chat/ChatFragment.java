package com.example.mentalhealthtracker.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealthtracker.R;
import com.google.ai.client.generativeai.BuildConfig;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.GenerateContentResponse;


import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatFragment extends Fragment {
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private FloatingActionButton sendButton;
    private ProgressBar progressBar;
    private ChatAdapter chatAdapter;
    private GenerativeModelFutures model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Gemini
        String apiKey = "ADD_API_KEY"; // Replace with your actual API key
        GenerationConfig.Builder config = new GenerationConfig.Builder();
                config.temperature = 0.7f;
                config.topK = 40;
                config.topP = 0.95f;

        ArrayList<SafetySetting> safetySettings = new ArrayList();
        safetySettings.add(new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE));
        safetySettings.add(new SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE));
        safetySettings.add(new SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE));
        safetySettings.add(new SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE));



        model = GenerativeModelFutures.from(new GenerativeModel("gemini-1.5-flash-001", apiKey, config.build(), safetySettings));
        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();

        // Add initial welcome message
        /*addMessage(new ChatMessage(
                "Hello! How are you feeling today?",
                ChatMessage.TYPE_AI
        ));*/
        String initialTraning = "You are an enthusiastic, encouraging, and caring mentor who helps people with mental health problems by understanding their hobbies and needs, and then helps them do some exercises to help with mental health and stress. Start by asking them how they are feeling and wait for them to respond after each question you ask. Then ask them how much time they have and give them one or two exercises to relieve stress. Find out what hobbies they have (e.g. Social, Physical, Intellectual or Spiritual), and then tailor the exercise to their hobbies and timeframe. Be kind and understanding and if they appear to be suffering a severe case of mental illness, direct them to professional help. Always be optimistic and end on a happy note.";
        generateResponse(initialTraning);

        return view;
    }

    private void initializeViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        chatAdapter = new ChatAdapter(requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) return;

        // Add user message
        addMessage(new ChatMessage(messageText, ChatMessage.TYPE_USER));
        messageEditText.setText("");

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);

        // Generate AI response using Gemini
        generateResponse(messageText);
    }

    private void generateResponse(String userMessage) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Content.Builder userMessageBuilder = new Content.Builder();
                userMessageBuilder.setRole("user");
                userMessageBuilder.addText(userMessage);

//                Publisher<GenerateContentResponse> streamingResponse =
//                        model.generateContentStream(userMessageBuilder.build());

                GenerateContentResponse response = model.generateContent(userMessageBuilder.build()).get();
                String aiResponse = response.getText();

                handler.post(() -> {
                    addMessage(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                    progressBar.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                });
            } catch (Exception e) {
                handler.post(() -> {
                    addMessage(new ChatMessage(
                            "I apologize, but I encountered an error. Please try again.",
                            ChatMessage.TYPE_AI
                    ));
                    progressBar.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                });
                e.printStackTrace();
            }
        });
    }

    private void addMessage(ChatMessage message) {
        chatAdapter.addMessage(message);
        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }
}
