package com.frapto.toterstest.model.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.frapto.toterstest.model.UserDao;
import com.frapto.toterstest.model.UsersDatabase;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;
import com.frapto.toterstest.model.entity.Message;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<PagedList<Message>> messageLivePagedList;

    public UserRepository(Application application) {
        UsersDatabase db = UsersDatabase.getDatabase(application);
        userDao = db.userDao();
    }
    public LiveData<List<ChatroomWithUserAndMessage>> getChatroomWithUserAndLastMessage(){
        return userDao.loadItems();
    }

    public void SendMessage(String messageString, long chatroomId, long senderId){
        Message message = new Message();
        message.setContent(messageString);
        message.setMessageChatroomId(chatroomId);
        message.setUserSenderId(senderId);
        message.setTimestamp(System.currentTimeMillis());
        UsersDatabase.databaseWriteExecutor.execute(()->{
            userDao.insertMessages(message);
        });
    }
    /**
     * supposedly should load items in pages of 10 but as mentioned before (in the other file)
     * the paging library is under documented, I can't really figure out the issue
     * it is either related to the recycler view stretching or to the paging library usage.
     * right now it simply returns everything
     * */
    public LiveData<PagedList<Message>> getMessages(long chatroomId) {
        DataSource.Factory fact =  userDao.getMessages(chatroomId);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build();
        messageLivePagedList = new LivePagedListBuilder<Integer, Message>(fact,config).build();
        return messageLivePagedList;
    }
    /**
     * gets the other person's id from the chatroom and sends a message as that user
     * */
    public void SendEchoMessage(String messageString, long chatroomId) {
        Message message = new Message();
        message.setContent(messageString);
        message.setMessageChatroomId(chatroomId);
        message.setTimestamp(System.currentTimeMillis());
        UsersDatabase.databaseWriteExecutor.execute(()->{
            long senderId = userDao.getOtherUserFromChatroom(chatroomId);
            message.setUserSenderId(senderId);
            userDao.insertMessages(message);
        });
    }
    public LiveData<Integer> getChatroomCount() {
        return userDao.getChatroomCount();
    }
}
