package com.example.mentalhealthtracker.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealthtracker.R;
import com.example.mentalhealthtracker.databinding.FragmentChatBinding;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter adapter;
    private ChatViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        setupRecyclerView();
        setupSendButton();
        observeMessages();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter();
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                viewModel.sendMessage(message);
                messageInput.setText("");
            }
        });
    }

    private void observeMessages() {
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter.submitList(new ArrayList<>(messages));
            chatRecyclerView.scrollToPosition(messages.size() - 1);
        });
    }
}