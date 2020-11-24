package com.frapto.toterstest.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.frapto.toterstest.model.entity.Message;

public class MessageDiffCallback extends DiffUtil.ItemCallback<Message> {
    @Override
    public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        return oldItem.getId()==newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
        return oldItem.getTimestamp()==newItem.getTimestamp();
    }
}
