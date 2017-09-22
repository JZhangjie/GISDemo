package com.demo.jzhangjie.gisdemo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.demo.jzhangjie.gisdemo.R;
import com.demo.jzhangjie.gisdemo.components.map.MapContract;
import com.demo.jzhangjie.gisdemo.components.map.MapFragment;
import com.demo.jzhangjie.gisdemo.components.map.MapPresenter;
import com.demo.jzhangjie.gisdemo.utils.PermissionHelper;

public class MainActivity extends AppCompatActivity {
    private MapContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionHelper.getPermission(MainActivity.this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        MapFragment fragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.main_map);
        if(fragment==null)
            fragment = new MapFragment();
        mPresenter = new MapPresenter(fragment);
        setSupportActionBar(toolbar);
    }
}
