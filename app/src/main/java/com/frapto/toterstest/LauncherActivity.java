package com.frapto.toterstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.frapto.toterstest.ui.main.MainViewModel;

public class LauncherActivity extends AppCompatActivity {
    MainViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        /**
         * does not return the same view model instance as the other activity, but we are only using it to access the repository
         * so it can initialize the database instead of sharing data with the other activity
         * */
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        /**
        * observe the count of chatrooms, and redirect when count >0
         * this ensures that on the first open, the database is initialized without showing a white screen
        * */
        mViewModel.getChatroomCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer>0){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        });

    }
}