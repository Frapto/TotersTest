package com.frapto.toterstest.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.frapto.toterstest.model.Repository.UserRepository;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;
import com.frapto.toterstest.model.entity.Message;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainViewModel extends AndroidViewModel {
    private LiveData<PagedList<Message>> messages;
    //indicates current chatroom so we know when to re-fetch data
    private long currentChatroomId = -1;
    private Random random = new Random();
    // to do work in background thread instead of main thread
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private UserRepository userRepository;
    public MainViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }
    public LiveData<Integer> getChatroomCount(){
        return userRepository.getChatroomCount();
    }
    public LiveData<List<ChatroomWithUserAndMessage>> getChatroomWithUserAndMessages(){
        return  userRepository.getChatroomWithUserAndLastMessage();
    }
    public void SendMessage(String message, long chatroomId, long senderId){
        //send the message
         userRepository.SendMessage(message, chatroomId, senderId);
         // 0 to 500 inclusive (501 is not included)
         long delay = random.nextInt(501);
         //echo the same message twice by the other user
         worker.schedule(() -> {
             userRepository.SendEchoMessage(message, chatroomId);
             userRepository.SendEchoMessage(message, chatroomId);
         }, delay, TimeUnit.MILLISECONDS);
    }
    public LiveData<PagedList<Message>> getMessages(long chatroomId){
        // if no data or user accessed a new chatroom, load data
        if(messages==null|| chatroomId!=currentChatroomId)
        {
            fetchMessages(chatroomId);
            currentChatroomId = chatroomId;
        }
        return messages;
    }
    public void fetchMessages(long chatroomId){
        messages = userRepository.getMessages(chatroomId);
    }
}