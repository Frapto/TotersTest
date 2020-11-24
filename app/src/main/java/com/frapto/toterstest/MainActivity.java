package com.frapto.toterstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.frapto.toterstest.ui.main.ActionBarActivity;
import com.frapto.toterstest.ui.main.MainViewModel;

/**
 * the activity is in an inactive state when the fragments are shown, this means it cannot listen to changes
 * of the viewmodel. In order to show/hide the action bar we resort to the interface technique where fragments can
 * get the instance in the attach method and hide it when they need to (and show it when they are paused/to-be-destroyed)
 * */
public class MainActivity extends AppCompatActivity implements ActionBarActivity {
    public MainViewModel mViewModel;
    private Toolbar ToolBarMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ToolBarMain = this.findViewById(R.id.tb_main);
        //set my custom toolbar as the action bar
        setSupportActionBar(ToolBarMain);
    }


    @Override
    public void hideActionBar() {
        ToolBarMain.setVisibility(View.GONE);
    }

    @Override
    public void showActionBar() {
        ToolBarMain.setVisibility(View.VISIBLE);
    }
}