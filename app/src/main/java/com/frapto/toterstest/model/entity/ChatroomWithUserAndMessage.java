package com.frapto.toterstest.model.entity;
import androidx.room.PrimaryKey;
/**
 * an entity to hold the display list (chatroom list) data
 * embedding the three entities is quicker but results in redundant fields like the ids
 * for that reason, unpacking the desired fields (also gives control to what the user has access to)
 * is more optimal
 * */
public class ChatroomWithUserAndMessage {
    @PrimaryKey
    private long chatroomId;
    private long otherUserId;
    public long userSenderId;
    private String content;
    private long timestamp;
    private String name;
    private String image;

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

    public long getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(long userSenderId) {
        this.userSenderId = userSenderId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
