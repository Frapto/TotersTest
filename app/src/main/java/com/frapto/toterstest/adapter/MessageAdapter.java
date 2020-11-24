package com.frapto.toterstest.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.frapto.toterstest.R;
import com.frapto.toterstest.model.entity.Message;
import com.frapto.toterstest.utils.Utilities;

import org.jetbrains.annotations.NotNull;

/**
 * PagedListAdapter and a few other API is deprecated but there is no real alternative for java
 * the paging library is still in alpha (in a real app, we would be looking for a more stable alternative).
 * The documentation for paging is severely lacking.
 * Android docs mentioned it is a good practice for paging (instead of the previously done scroll listeners and database limits/offsets)
 * but it does not seem ready to be yet.
 * */
public class MessageAdapter extends PagedListAdapter<Message, MessageAdapter.MessageViewHolder> {
    protected MessageAdapter(@NotNull DiffUtil.ItemCallback<Message> diffCallback) {
        super(diffCallback);
    }
    public MessageAdapter(){
        this(new MessageDiffCallback());
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = getItem(position);
        if(message!=null){
            holder.bind(message);
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        private TextView TextViewMessage;
        private TextView TextViewTimestamp;
        private LinearLayout LinearLayoutMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewMessage = itemView.findViewById(R.id.tv_chat_message);
            TextViewTimestamp = itemView.findViewById(R.id.tv_chat_timestamp);
            LinearLayoutMessage = itemView.findViewById(R.id.ll_message);
        }
        public void bind(Message message){
            TextViewMessage.setText(message.getContent());
            String dateString = Utilities.convertToMessageTime(message.getTimestamp());
            TextViewTimestamp.setText(dateString);
            if(Utilities.MY_ID == message.userSenderId){
                LinearLayoutMessage.setBackgroundColor(Color.parseColor(Utilities.SENDER_COLOR));
                //move right
                ConstraintLayout.LayoutParams clp = (ConstraintLayout.LayoutParams) LinearLayoutMessage.getLayoutParams();
                clp.horizontalBias = 0.9f;
                LinearLayoutMessage.setLayoutParams(clp);
            } else {
                LinearLayoutMessage.setBackgroundColor(Color.parseColor(Utilities.RECEIVER_COLOR));
                //move left
                ConstraintLayout.LayoutParams clp = (ConstraintLayout.LayoutParams) LinearLayoutMessage.getLayoutParams();
                clp.horizontalBias = 0.1f;
                LinearLayoutMessage.setLayoutParams(clp);
            }
        }
    }

}
