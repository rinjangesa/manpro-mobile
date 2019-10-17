package com.jc.allnewjcclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.customs.CustomSound;

public class DetailGrammarActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private WebView webView;
    private TextView titleTxt;
    private Button backBtn;

    // Custom
    private CustomSound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_grammar);

        String wallData = getIntent().getStringExtra("WALLPAPER");
        String title = getIntent().getStringExtra("TITLE");
        String desc = getIntent().getStringExtra("DESC");

        sound = new CustomSound(DetailGrammarActivity.this);

        findViewId();

        onClickSetting();

        titleTxt.setText(title);

        webView.loadData(desc, "text/html", "utf-8");

        Glide.with(DetailGrammarActivity.this).load(wallData).into(wallView);
    }

    private void findViewId() {
        wallView = findViewById(R.id.detail_grammar_wallpaper);
        webView = findViewById(R.id.detail_grammar_desc);
        titleTxt = findViewById(R.id.detail_grammar_title);
        backBtn = findViewById(R.id.back_btn);
    }

    private void onClickSetting() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
    }
}
