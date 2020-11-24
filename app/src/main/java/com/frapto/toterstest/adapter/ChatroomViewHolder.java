package com.frapto.toterstest.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frapto.toterstest.R;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;
import com.frapto.toterstest.utils.Utilities;

/**
 * defines the view holder for the recycler view of chatroom items
 * */
public class ChatroomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView TVName;
    private final ImageView IVProfile;
    private final TextView TVLastMessage;
    private final TextView TVLastMessageTimestamp;
    private final ListItemClickListener listener;
    public ChatroomViewHolder(@NonNull View itemView, ListItemClickListener listener) {
        super(itemView);
        TVName = itemView.findViewById(R.id.tv_user_name);
        IVProfile = itemView.findViewById(R.id.iv_profile_pic);
        TVLastMessage = itemView.findViewById(R.id.tv_last_message);
        TVLastMessageTimestamp = itemView.findViewById(R.id.tv_last_message_timestamp);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(ChatroomWithUserAndMessage item) {
        TVName.setText(item.getName());
        TVLastMessage.setText(item.getContent());
        long time = item.getTimestamp();
        String dateString = Utilities.convertToMessageTime(time);
        TVLastMessageTimestamp.setText(dateString);
        IVProfile.setImageURI(Uri.parse(item.getImage()));
    }

     static ChatroomViewHolder create(ViewGroup parent, ListItemClickListener listener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatroom_view_item, parent, false);
        return new ChatroomViewHolder(view, listener);
    }

    @Override
    public void onClick(View v) {
        listener.onListItemClick(v);
    }
}
