package com.jc.allnewjcclass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.customs.CustomDialog;

import java.io.StringReader;

public class DetailActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private Button backBtn;
    private LottieAnimationView lottieAnimationView;
    private TextView kanjiStroke, kanjiVar1, kanjiVar2, kanjiVar3, kanjiVar4, kanjiVar5, kanjiMeaning,
            kanjiExample, kanjiOnyomi, kanjiKunyomi;

    // Custom
    private CustomDialog dialog;

    // Data
    private String wallData, idKanji;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idKanji = getIntent().getStringExtra("ID");
        wallData = getIntent().getStringExtra("WALLPAPER");
        bundle = getIntent().getBundleExtra("BUNDLE");

        dialog = new CustomDialog(DetailActivity.this);

        findViewId();

        onClickSetting();

        setBundle();

        Glide.with(DetailActivity.this).load(wallData).into(wallView);
    }

    private void findViewId() {
        wallView = findViewById(R.id.detail_wallpaper);
        backBtn = findViewById(R.id.back_btn);
        lottieAnimationView = findViewById(R.id.detail_kanji_animation);
        kanjiVar1 = findViewById(R.id.detail_kanji1);
        kanjiVar2 = findViewById(R.id.detail_kanji2);
        kanjiVar3 = findViewById(R.id.detail_kanji3);
        kanjiVar4 = findViewById(R.id.detail_kanji4);
        kanjiVar5 = findViewById(R.id.detail_kanji5);
        kanjiMeaning = findViewById(R.id.detail_kanji_meaning);
        kanjiOnyomi = findViewById(R.id.detail_kanji_onyomi);
        kanjiKunyomi = findViewById(R.id.detail_kanji_kunyomi);
        kanjiStroke = findViewById(R.id.detail_kanji_stroke);
        kanjiExample = findViewById(R.id.detail_kanji_example);
    }

    private void onClickSetting() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setBundle() {
        String kanji = bundle.getString("FIELD");
        String meaning = "Meaning : " + bundle.getString("MEANING");
        String stroke = "Stroke : " + bundle.getString("STROKE");
        String animation = bundle.getString("ANIMATION");
        String onyomi = "Onyomi reading : " + bundle.getString("ONYOMI");
        String kunyomi = "Kunyomi reading : " + bundle.getString("KUNYOMI");
        String example = "Example : " + bundle.getString("EXAMPLE");

        kanjiVar1.setText(kanji);
        kanjiVar2.setText(kanji);
        kanjiVar3.setText(kanji);
        kanjiVar4.setText(kanji);
        kanjiVar5.setText(kanji);
        kanjiMeaning.setText(meaning);
        kanjiStroke.setText(stroke);
        kanjiOnyomi.setText(onyomi);
        kanjiKunyomi.setText(kunyomi);
        kanjiExample.setText(example);

        JsonReader jsonReader;
        if (animation != null) {
            jsonReader = new JsonReader(new StringReader(animation));
            lottieAnimationView.setAnimation(jsonReader);
            lottieAnimationView.playAnimation();
        }
    }
}
