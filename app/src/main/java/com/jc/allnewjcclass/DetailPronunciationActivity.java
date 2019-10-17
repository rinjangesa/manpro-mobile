package com.jc.allnewjcclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.customs.CustomSound;

public class DetailPronunciationActivity extends AppCompatActivity {

    // Custom
    private CustomSound sound;

    // Data
    private String wallData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pronunciation);

        wallData = getIntent().getStringExtra("WALLPAPER");
        String title = getIntent().getStringExtra("TITLE");
        String desc = getIntent().getStringExtra("DESC");

        sound = new CustomSound(DetailPronunciationActivity.this);

        ImageView wallView = findViewById(R.id.detail_pronuncitaion_wallpaper);
        WebView webView = findViewById(R.id.detail_pronunciation_desc);
        Button backBtn = findViewById(R.id.back_btn);
        TextView titleText = findViewById(R.id.detail_pronunciation_title);

        titleText.setText(title);

        webView.loadData(desc, "text/html", "utf-8");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });

        Glide.with(DetailPronunciationActivity.this).load(wallData).into(wallView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
