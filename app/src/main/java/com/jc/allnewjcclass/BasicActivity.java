package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.jc.allnewjcclass.adapters.KanaRecyclerAdapter;
import com.jc.allnewjcclass.customs.CustomSound;

import io.paperdb.Paper;

public class BasicActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private RecyclerView recyclerView;
    private Button backBtn;
    private RadioGroup radioGroup;

    // Data
    private String hiraganaSeion[] = {"あ", "い", "う", "え", "お",
            "か", "き", "く", "け", "こ",
            "さ", "し", "す", "せ", "そ",
            "た", "ち", "つ", "て", "と",
            "な", "に", "ぬ", "ね", "の",
            "は", "ひ", "ふ", "へ", "ほ",
            "ま", "み", "む", "め", "も",
            "や", " ", "ゆ", " ", "よ",
            "ら", "り", "る", "れ", "ろ",
            "わ", " ", " ", " ", "を", "ん"
    };

    private String hiraganaSeionStroke[] = {"3", "2", "2", "2", "3",
            "3", "3", "2", "3", "2",
            "2", "1", "2", "3", "1",
            "3", "3", "2", "3", "2",
            "4", "3", "2", "3", "1",
            "3", "1", "4", "1", "4",
            "3", "2", "3", "2", "3",
            "3", " ", "2", " ", "2",
            "2", "2", "1", "2", "1",
            "2", " ", " ", " ", "3", "1"
    };

    private String katakanaSeion[] = {"ア", "イ", "ウ", "エ", "オ",
            "カ", "キ", "ク", "ケ", "コ",
            "サ", "シ", "ス", "セ", "ソ",
            "タ", "チ", "ツ", "テ", "ト",
            "ナ", "ニ", "ヌ", "ネ", "ノ",
            "八", "ヒ", "フ", "ヘ", "ホ",
            "マ", "ミ", "ム", "メ", "モ",
            "ヤ", " ", "ユ", " ", "ヨ",
            "ラ", "リ", "ル", "レ", "ロ",
            "ワ", " ", " ", " ", "ヲ", "ン"
    };

    private String katakanaSeionStroke[] = {"2", "2", "2", "3", "3",
            "2", "3", "2", "3", "2",
            "3", "3", "2", "2", "2",
            "3", "3", "3", "3", "2",
            "2", "2", "2", "4", "1",
            "2", "2", "1", "1", "4",
            "2", "3", "2", "2", "1",
            "2", " ", "2", " ", "3",
            "2", "2", "2", "1", "3",
            "1", " ", " ", " ", "2", "2"
    };

    private int animationHiraganaSeion[] = {
            R.raw.hiragana_a, R.raw.hiragana_i, R.raw.hiragana_u, R.raw.hiragana_e, R.raw.hiragana_o,
            R.raw.hiragana_ka, R.raw.hiragana_ki, R.raw.hiragana_ku, R.raw.hiragana_ke, R.raw.hiragana_ko,
            R.raw.hiragana_sa, R.raw.hiragana_shi, R.raw.hiragana_su, R.raw.hiragana_se, R.raw.hiragana_so,
            R.raw.hiragana_ta, R.raw.hiragana_chi, R.raw.hiragana_tsu, R.raw.hiragana_te, R.raw.hiragana_to,
            R.raw.hiragana_na, R.raw.hiragana_ni, R.raw.hiragana_nu, R.raw.hiragana_ne, R.raw.hiragana_no,
            R.raw.hiragana_ha, R.raw.hiragana_hi, R.raw.hiragana_fu, R.raw.hiragana_he, R.raw.hiragana_ho,
            R.raw.hiragana_ma, R.raw.hiragana_mi, R.raw.hiragana_mu, R.raw.hiragana_me, R.raw.hiragana_mo,
            R.raw.hiragana_ya, 0, R.raw.hiragana_yu, 0, R.raw.hiragana_yo,
            R.raw.hiragana_ra, R.raw.hiragana_ri, R.raw.hiragana_ru, R.raw.katakana_re, R.raw.hiragana_ro,
            R.raw.hiragana_wa, 0, 0, 0, R.raw.hiragana_wo, R.raw.hiragana_n
    };

    private int animationKatakanaSeion[] = {
            R.raw.katakana_a, R.raw.katakana_i, R.raw.katakana_u, R.raw.katakana_e, R.raw.katakana_o,
            R.raw.katakana_ka, R.raw.katakana_ki, R.raw.katakana_ku, R.raw.katakana_ke, R.raw.katakana_ko,
            R.raw.katakana_sa, R.raw.katakana_shi, R.raw.katakana_su, R.raw.katakana_se, R.raw.katakana_so,
            R.raw.katakana_ta, R.raw.katakana_chi, R.raw.katakana_tsu, R.raw.katakana_te, R.raw.katakana_to,
            R.raw.katakana_na, R.raw.katakana_ni, R.raw.katakana_nu, R.raw.katakana_ne, R.raw.katakana_no,
            R.raw.katakana_ha, R.raw.katakana_hi, R.raw.katakana_fu, R.raw.katakana_he, R.raw.katakana_ho,
            R.raw.katakana_ma, R.raw.katakana_mi, R.raw.katakana_mu, R.raw.katakana_me, R.raw.katakana_mo,
            R.raw.katakana_ya, 0, R.raw.katakana_yu, 0, R.raw.katakana_yo,
            R.raw.katakana_ra, R.raw.katakana_ri, R.raw.katakana_ru, R.raw.katakana_re, R.raw.katakana_ro,
            R.raw.katakana_wa, 0, 0, 0, R.raw.katakana_wo, R.raw.katakana_n
    };

    private int soundSeion[] = {R.raw.a, R.raw.i, R.raw.u, R.raw.e, R.raw.o,
            R.raw.ka, R.raw.ki, R.raw.ku, R.raw.ke, R.raw.ko,
            R.raw.sa, R.raw.shi, R.raw.su, R.raw.se, R.raw.so,
            R.raw.ta, R.raw.chi, R.raw.tsu, R.raw.te, R.raw.to,
            R.raw.na, R.raw.ni, R.raw.nu, R.raw.ne, R.raw.no,
            R.raw.ha, R.raw.hi, R.raw.fu, R.raw.he, R.raw.ho,
            R.raw.ma, R.raw.mi, R.raw.mu, R.raw.me, R.raw.mo,
            R.raw.ya, 0, R.raw.yu, 0, R.raw.yo,
            R.raw.ra, R.raw.ri, R.raw.ru, R.raw.re, R.raw.ro,
            R.raw.wa, 0, 0, 0, R.raw.wo, R.raw.n};

    private String letterSeion[] = {"a", "i", "u", "e", "o", "ka", "ki", "ku", "ke", "ko",
            "sa", "shi", "su", "se", "so", "ta", "chi", "tsu", "te", "to",
            "na", "ni", "nu", "ne", "no", "ha", "hi", "fu", "he", "ho"
            , "ma", "mi", "mu", "me", "mo", "ya", " ", "yu", " ", "yo",
            "ra", "ri", "ru", "re", "ro", "wa", " "
            , " ", " ", "wo", "n"};

    private String letterDakuon[] = {"ga", "gi", "gu", "ge", "go",
            "za", "ji", "zu", "ze", "zo",
            "da", "di", "du", "de", "do",
            "ba", "bi", "bu", "be", "bo",
            "pa", "pi", "pu", "pe", "po"};

    // Custom
    private CustomSound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Paper.init(BasicActivity.this);

        String wallData = getIntent().getStringExtra("WALLPAPER");

        sound = new CustomSound(BasicActivity.this);

        wallView = findViewById(R.id.basic_wallpaper);
        recyclerView = findViewById(R.id.list_kana);
        radioGroup = findViewById(R.id.radio_kana);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(BasicActivity.this, 5));
        recyclerView.setAdapter(new KanaRecyclerAdapter(BasicActivity.this, hiraganaSeion, hiraganaSeionStroke, letterSeion, animationHiraganaSeion, soundSeion));

        Glide.with(BasicActivity.this).load(wallData).into(wallView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopService(new Intent(BasicActivity.this, CustomSound.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        String mute = Paper.book().read("MUTE");
        if (mute != null && mute.equals("OFF")) {
            startService(new Intent(BasicActivity.this, CustomSound.class));
        } else if (mute != null && mute.equals("ON")) {
            stopService(new Intent(BasicActivity.this, CustomSound.class));
        }
    }

    public void onRadioButtonClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_hiragana:
                if (checked)
                    recyclerView.setAdapter(new KanaRecyclerAdapter(BasicActivity.this, hiraganaSeion, hiraganaSeionStroke, letterSeion, animationHiraganaSeion, soundSeion));
                break;
            case R.id.radio_katakana:
                if (checked)
                    recyclerView.setAdapter(new KanaRecyclerAdapter(BasicActivity.this, katakanaSeion, katakanaSeionStroke, letterSeion, animationKatakanaSeion, soundSeion));
                break;
        }
    }
}
