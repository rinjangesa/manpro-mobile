package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.customs.CustomSound;

public class GrammarActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private Button endlessGrammar, basicBtn, intermediateBtn, expertBtn, masterBtn;
    private ImageButton homeBtn;

    // Custom
    private CustomSound sound;

    // Data
    private String wallData;
    private int levelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        wallData = getIntent().getStringExtra("WALLPAPER");
        levelUser = getIntent().getIntExtra("USER_LEVEL", 0);

        sound = new CustomSound(GrammarActivity.this);

        findViewId();

        enablingButton(levelUser);

        onClickSetting();

        Glide.with(GrammarActivity.this).load(wallData).into(wallView);
    }

    private void findViewId() {
        wallView = findViewById(R.id.grammar_wallpaper);
        homeBtn = findViewById(R.id.btn_home);
        endlessGrammar = findViewById(R.id.endless_grammar);
        basicBtn = findViewById(R.id.grammar_basic);
        intermediateBtn = findViewById(R.id.grammar_inter);
        expertBtn = findViewById(R.id.grammar_expert);
        masterBtn = findViewById(R.id.grammar_master);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
        endlessGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(GrammarActivity.this, EndlessActivity.class);
                intent.putExtra("COLLECTION", "grammar_quiz");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });
        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Bundle b = new Bundle();
                b.putString("COLLECTION", "grammar_quiz");
                b.putString("FIELD", "basic");
                Intent intent = new Intent(GrammarActivity.this, QuizActivity.class);
                intent.putExtra("ID", "id");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", b);
                startActivity(intent);
            }
        });
        intermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Bundle b = new Bundle();
                b.putString("COLLECTION", "grammar_quiz");
                b.putString("FIELD", "intermediate");
                Intent intent = new Intent(GrammarActivity.this, QuizActivity.class);
                intent.putExtra("ID", "id");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", b);
                startActivity(intent);
            }
        });
        expertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Bundle b = new Bundle();
                b.putString("COLLECTION", "grammar_quiz");
                b.putString("FIELD", "expert");
                Intent intent = new Intent(GrammarActivity.this, QuizActivity.class);
                intent.putExtra("ID", "id");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", b);
                startActivity(intent);
            }
        });
        masterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Bundle b = new Bundle();
                b.putString("COLLECTION", "grammar_quiz");
                b.putString("FIELD", "master");
                Intent intent = new Intent(GrammarActivity.this, QuizActivity.class);
                intent.putExtra("ID", "id");
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", b);
                startActivity(intent);
            }
        });
    }

    private void enablingButton(int levelUser) {
        levelUser = this.levelUser;
        intermediateBtn.setEnabled(false);
        expertBtn.setEnabled(false);
        masterBtn.setEnabled(false);
        if (50 <= levelUser && levelUser < 75) {
            intermediateBtn.setEnabled(true);
            expertBtn.setEnabled(false);
            masterBtn.setEnabled(false);
        } else if (75 <= levelUser && levelUser < 100) {
            intermediateBtn.setEnabled(true);
            expertBtn.setEnabled(true);
            masterBtn.setEnabled(false);
        } else if (100 <= levelUser) {
            intermediateBtn.setEnabled(true);
            expertBtn.setEnabled(true);
            masterBtn.setEnabled(true);
        }
    }
}
