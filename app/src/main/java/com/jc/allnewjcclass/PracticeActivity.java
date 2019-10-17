package com.jc.allnewjcclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PracticeActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;

    // Data
    private String wallData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        wallData = getIntent().getStringExtra("WALLPAPER");

        findViewId();

        onClickSetting();

        Glide.with(PracticeActivity.this).load(wallData).into(wallView);
    }

    private void findViewId() {
        wallView = findViewById(R.id.practice_wallpaper);
    }

    private void onClickSetting() {
    }
}
