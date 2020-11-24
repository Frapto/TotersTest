package com.frapto.toterstest.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "otherUserId"),
        indices = {
                @Index(value = {"otherUserId"})
        })
public class Chatroom {
    @PrimaryKey(autoGenerate = true)
    private long chatroomId;
    private long otherUserId;

    public long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(long otherUserId) {
        this.otherUserId = otherUserId;
    }
}
