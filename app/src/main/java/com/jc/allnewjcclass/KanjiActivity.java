package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.customs.CustomSound;

public class KanjiActivity extends AppCompatActivity {

    // UI
    private Button endless, n5, n4, n3, n2, n1;
    private ImageButton homeBtn;
    private ImageView wallView;

    // Custom
    private CustomSound sound;

    // Data
    private String wallData;
    private int levelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji);

        wallData = getIntent().getStringExtra("WALLPAPER");
        levelUser = getIntent().getIntExtra("USER_LEVEL", 0);

        sound = new CustomSound(KanjiActivity.this);

        findViewId();

        enablingButton(levelUser);

        onClickSetting();

        Glide.with(KanjiActivity.this).load(wallData).into(wallView);
    }

    private void findViewId() {
        homeBtn = findViewById(R.id.btn_home);
        endless = findViewById(R.id.endless_kanji);
        wallView = findViewById(R.id.kanji_wallpaper);
        endless = findViewById(R.id.endless_kanji);
        n5 = findViewById(R.id.kanji_n5);
        n4 = findViewById(R.id.kanji_n4);
        n3 = findViewById(R.id.kanji_n3);
        n2 = findViewById(R.id.kanji_n2);
        n1 = findViewById(R.id.kanji_n1);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
        endless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, EndlessActivity.class);
                intent.putExtra("COLLECTION", "kanji_quiz");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });
        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, KanjiListActivity.class);
                intent.putExtra("LEVEL", "N5");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, KanjiListActivity.class);
                intent.putExtra("LEVEL", "N4");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, KanjiListActivity.class);
                intent.putExtra("LEVEL", "N3");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, KanjiListActivity.class);
                intent.putExtra("LEVEL", "N2");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(KanjiActivity.this, KanjiListActivity.class);
                intent.putExtra("LEVEL", "N1");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });
    }

    private void enablingButton(int levelUser) {
        levelUser = this.levelUser;
        n4.setEnabled(false);
        n3.setEnabled(false);
        n2.setEnabled(false);
        n1.setEnabled(false);
        if (20 <= levelUser && levelUser < 40) {
            n4.setEnabled(true);
            n3.setEnabled(false);
            n2.setEnabled(false);
            n1.setEnabled(false);
        } else if (40 <= levelUser && levelUser < 60) {
            n4.setEnabled(true);
            n3.setEnabled(true);
            n2.setEnabled(false);
            n1.setEnabled(false);
        } else if (60 <= levelUser && levelUser < 80) {
            n4.setEnabled(true);
            n3.setEnabled(true);
            n2.setEnabled(true);
            n1.setEnabled(false);
        } else if (80 <= levelUser) {
            n4.setEnabled(true);
            n3.setEnabled(true);
            n2.setEnabled(true);
            n1.setEnabled(true);
        }
    }
}
