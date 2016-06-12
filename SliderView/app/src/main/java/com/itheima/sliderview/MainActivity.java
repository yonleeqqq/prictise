package com.itheima.sliderview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.itheima.sliderview.ui.SlideMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlideMenu sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sm = (SlideMenu) findViewById(R.id.sm);
        findViewById(R.id.ib_slide_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sm.switchState();
    }
}
