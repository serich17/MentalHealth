package com.example.mentalhealthtracker.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

                public class ChatViewModel extends ViewModel {
                    private static final String API_KEY = "AIzaSyAmstcWaJsoHMtfzoZw0bYeMZWg4jKf9vU";
                    private final GenerativeModel model;
                    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
                    private final Executor executor = Executors.newSingleThreadExecutor();

                    public ChatViewModel() {
                        model = new GenerativeModel("gemini-pro", API_KEY);
                    }

                    public LiveData<List<ChatMessage>> getMessages() {
                        return messages;
                    }

                    public void sendMessage(String userMessage) {
                        List<ChatMessage> currentMessages = messages.getValue();
                        currentMessages.add(new ChatMessage(userMessage, true));
                        messages.setValue(currentMessages);

                        generateResponse(userMessage);
                    }

                    private void generateResponse(String userMessage) {
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                // Create content using the builder pattern
                                Content content = new Content.Builder()
                                        .addText(userMessage)
                                        .build();

                                // Generate the response
                                GenerateContentResponse response = model.generateContent(content);

                                return response.getText();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }, executor).thenAccept(result -> {
                            if (result != null) {
                                List<ChatMessage> currentMessages = messages.getValue();
                                currentMessages.add(new ChatMessage(result, false));
                                messages.postValue(currentMessages);
                            } else {
                                List<ChatMessage> currentMessages = messages.getValue();
                                currentMessages.add(new ChatMessage("Sorry, I couldn't generate a response.", false));
                                messages.postValue(currentMessages);
                            }
                        });
                    }


                }

