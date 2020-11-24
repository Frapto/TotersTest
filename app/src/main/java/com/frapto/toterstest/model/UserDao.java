package com.frapto.toterstest.model;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.frapto.toterstest.model.entity.Chatroom;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;
import com.frapto.toterstest.model.entity.Message;
import com.frapto.toterstest.model.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    /**
     * returns the chatroom, user name, user image, last message, last message sender, and last message timestamp
     * for each chatroom. Users who do not have previous chats have empty sender, message, and timestamp.
     * Relations do not seem to do these sort of complex queries easily, so using foreign keys and doing it in manual sql was quicker
     * */
    @Query("select ch.*, us.name, us.image, m3.content as content, m3.userSenderId, m3.timestamp from chatroom ch inner join user us on ch.otherUserId = us.userId" +
            " left join (select m1.content, m1.timestamp, m1.messageChatroomId, m1.userSenderId from message m1 where m1.timestamp = (select max(m2.timestamp)" +
            " from message m2 where m2.messageChatroomId = m1.messageChatroomId)) m3 on ch.chatroomId = m3.messageChatroomId " +
            "order by m3.timestamp desc")
    LiveData<List<ChatroomWithUserAndMessage>> loadItems();

    /**
     * returns the id of the user associated with a chatroom
     * */
    @Query("select userId from User inner join chatroom on userId=otherUserId where chatroomId= :chatroomId")
    long getOtherUserFromChatroom(long chatroomId);

    /**
     * supposedly returns a source for the paging to use, seems to return the full list currently
     * */
    @Query("select * from message where messageChatroomId = :chatroomId order by timestamp desc")
    DataSource.Factory<Integer, Message> getMessages(long chatroomId);

    /**
     *  gets the number of chatroom, is used to determine that the database is initialized (count > 0)
    */
    @Query("select count(chatroomId) from chatroom")
    LiveData<Integer> getChatroomCount();

    @Insert
    void insertChatrooms(Chatroom... chatrooms);
    /**
     * inserts users and returns their ids as an array
     * */
    @Insert
    long[] insertUsers(User... users);
    @Insert
    void insertMessages(Message... messages);
}
