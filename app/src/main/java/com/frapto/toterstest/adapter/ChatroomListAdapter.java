package com.frapto.toterstest.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.frapto.toterstest.R;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;

public class ChatroomListAdapter extends ListAdapter<ChatroomWithUserAndMessage, ChatroomViewHolder> {

    private ListItemClickListener listener;
    public ChatroomListAdapter(@NonNull DiffUtil.ItemCallback<ChatroomWithUserAndMessage> diffCallback, ListItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ChatroomViewHolder.create(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position) {
        ChatroomWithUserAndMessage item = getItem(position);
        holder.bind(item);
        //the tags identify which item was clicked so we can send it to the chat fragment
        holder.itemView.setTag(R.id.chatroomId,item.getChatroomId());
        holder.itemView.setTag(R.id.otherPersonName,item.getName());
    }
    public static class ChatroomDiff extends DiffUtil.ItemCallback<ChatroomWithUserAndMessage>{

        @Override
        public boolean areItemsTheSame(@NonNull ChatroomWithUserAndMessage oldItem, @NonNull ChatroomWithUserAndMessage newItem) {
            return oldItem.getChatroomId() == newItem.getChatroomId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatroomWithUserAndMessage oldItem, @NonNull ChatroomWithUserAndMessage newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp();
        }
    }
}
