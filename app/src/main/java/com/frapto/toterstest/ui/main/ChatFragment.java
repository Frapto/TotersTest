package com.frapto.toterstest.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.frapto.toterstest.R;
import com.frapto.toterstest.adapter.MessageAdapter;
import com.frapto.toterstest.model.entity.Message;
import com.frapto.toterstest.utils.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ChatFragment extends Fragment {
    private ActionBarActivity actionBarActivity;
    private static final String CHAT_BOX_KEY = "CHAT_BOX_KEY";
    private static final String RECYCLER_CHAT_KEY = "RECYCLER_CHAT_KEY";
    private EditText EditTextChatBox;
    private FloatingActionButton SendButton;
    long chatroomId;
    private RecyclerView RecyclerViewChat;
    public ChatFragment() {
        // Required empty public constructor
    }

    private MainViewModel mViewModel;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getMessages(chatroomId).observe(getViewLifecycleOwner(), new Observer<PagedList<Message>>() {
            @Override
            public void onChanged(PagedList<Message> messages) {
                MessageAdapter adapter = (MessageAdapter)RecyclerViewChat.getAdapter();
                adapter.submitList(messages);
                if(adapter.getItemCount()>0) {

                    //so it scrolls after data has been loaded, doesn't always work, it may have to do with the pagination issues
                    if (savedInstanceState != null) {
                        Parcelable liststate = savedInstanceState.getParcelable(RECYCLER_CHAT_KEY);
                        if (liststate != null) {
                            //scroll to last position in case of rotation
                            RecyclerViewChat.getLayoutManager().onRestoreInstanceState(liststate);
                            savedInstanceState.remove(RECYCLER_CHAT_KEY);
                        } else {
                            //scroll to newest items
                            LinearLayoutManager llm = (LinearLayoutManager) RecyclerViewChat.getLayoutManager();
                            llm.smoothScrollToPosition(RecyclerViewChat,null,0);
                        }
                    } else {
                        //scroll to bottom
                        LinearLayoutManager llm = (LinearLayoutManager) RecyclerViewChat.getLayoutManager();
                        llm.smoothScrollToPosition(RecyclerViewChat,null,0);
                    }
                }

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton backButton = view.findViewById(R.id.btn_back_arrow);
        EditTextChatBox = view.findViewById(R.id.ed_chatbox);
        SendButton = view.findViewById(R.id.btn_send);
        RecyclerViewChat = view.findViewById(R.id.rv_chat);
        chatroomId = ChatFragmentArgs.fromBundle(getArguments()).getChatroomId();
        String personName = ChatFragmentArgs.fromBundle(getArguments()).getOtherPersonName();
        TextView TextViewChatName = view.findViewById(R.id.tv_chat_name);
        TextViewChatName.setText(personName);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //allows the recycler view to start from the bottom
        llm.setReverseLayout(true);
        RecyclerViewChat.setLayoutManager(llm);
        MessageAdapter messageAdapter = new MessageAdapter();
        RecyclerViewChat.setAdapter(messageAdapter);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.SendMessage(EditTextChatBox.getText().toString(),chatroomId,Utilities.MY_ID);
                EditTextChatBox.setText("", TextView.BufferType.EDITABLE);
                Utilities.hideKeyboardFrom(getContext(), getView().getRootView());
            }
        });
        //in case of rotation or phone call, restore the typed unsent text
        if(savedInstanceState!=null){
            String prev = savedInstanceState.getString(CHAT_BOX_KEY, "");
            EditTextChatBox.setText(prev, TextView.BufferType.EDITABLE);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        actionBarActivity = (ActionBarActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        actionBarActivity.hideActionBar();
    }

    @Override
    public void onPause() {
        Utilities.hideKeyboardFrom(getContext(), getView().getRootView());
        actionBarActivity.showActionBar();
        super.onPause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CHAT_BOX_KEY, EditTextChatBox.getText().toString());
        Parcelable listState = RecyclerViewChat.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_CHAT_KEY,listState);
        super.onSaveInstanceState(outState);
    }


}