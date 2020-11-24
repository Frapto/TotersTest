package com.frapto.toterstest.model.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userSenderId"),
                @ForeignKey(entity = Chatroom.class, parentColumns = "chatroomId", childColumns = "messageChatroomId")
        },
        indices = {
                @Index(value = {"userSenderId"}),
                @Index(value = {"messageChatroomId"})
        }
)
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;
    public long userSenderId;
    private long messageChatroomId;
    public String content;
    public long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(long userSenderId) {
        this.userSenderId = userSenderId;
    }

    public long getMessageChatroomId() {
        return messageChatroomId;
    }

    public void setMessageChatroomId(long chatroomId) {
        this.messageChatroomId = chatroomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
