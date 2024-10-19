package com.example.mentalhealthtracker.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mentalhealthtracker.R;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<ChatMessage> messages = new ArrayList<>();
    private final Context context;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageText.setText(message.getMessage());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageText.getLayoutParams();

        if (message.getMessageType() == ChatMessage.TYPE_USER) {
            holder.messageText.setBackground(ContextCompat.getDrawable(context, R.drawable.user_message_background));
            holder.messageText.setTextColor(Color.WHITE);
            layoutParams.gravity = Gravity.END;
        } else {
            holder.messageText.setBackground(ContextCompat.getDrawable(context, R.drawable.ai_message_background));
            holder.messageText.setTextColor(Color.BLACK);
            layoutParams.gravity = Gravity.START;
        }

        holder.messageText.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageTextView);
        }
    }
}