package com.frapto.toterstest.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frapto.toterstest.R;
import com.frapto.toterstest.adapter.ChatroomListAdapter;
import com.frapto.toterstest.adapter.ListItemClickListener;
import com.frapto.toterstest.model.entity.ChatroomWithUserAndMessage;

import java.util.List;

public class MainFragment extends Fragment implements ListItemClickListener {

    private MainViewModel mViewModel;
    private static final String RECYCLER_LIST_KEY = "RECYCLER_LIST_KEY";
    RecyclerView RecyclerViewChatroom;
    private NavController navController = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        mViewModel.getChatroomWithUserAndMessages().observe(getViewLifecycleOwner(), new Observer<List<ChatroomWithUserAndMessage>>() {
            @Override
            public void onChanged(List<ChatroomWithUserAndMessage> chatrooms) {
                ChatroomListAdapter adapter = (ChatroomListAdapter) RecyclerViewChatroom.getAdapter();
                adapter.submitList(chatrooms);
                if(savedInstanceState!=null){
                    Parcelable listState = savedInstanceState.getParcelable(RECYCLER_LIST_KEY);
                    RecyclerViewChatroom.getLayoutManager().onRestoreInstanceState(listState);
                }
            }
        });
    }
    /**
     * saves the position of the scrollbar in case rotation occurs
     * */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable listState = RecyclerViewChatroom.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_LIST_KEY,listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerViewChatroom = view.findViewById(R.id.rv_chat);
        navController = Navigation.findNavController(view);
        final ChatroomListAdapter adapter = new ChatroomListAdapter(new ChatroomListAdapter.ChatroomDiff(), this);
        RecyclerViewChatroom.setAdapter(adapter);
        RecyclerViewChatroom.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onListItemClick(View v) {
        Long chatroomId = Long.parseLong(v.getTag(R.id.chatroomId).toString());
        String otherPersonName = v.getTag(R.id.otherPersonName).toString();
        MainFragmentDirections.ActionMainFragmentToChatFragment actionMainFragmentToChatFragment = MainFragmentDirections.actionMainFragmentToChatFragment(chatroomId, otherPersonName);
        navController.navigate(actionMainFragmentToChatFragment);
    }
}