package com.jc.allnewjcclass;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;
import com.jc.allnewjcclass.helpers.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    // UI
    private LinearLayout header, menu, footer;
    private ImageButton kanjiBtn, phraseBtn, grammarBtn, shopBtn, itemBtn, basicBtn, eventBtn, rankBtn, settingBtn, profileBtn;
    private ImageView charView, wallView, decorView;
    private TextView levelTxt, moneyTxt;
    private ProgressBar expBar;
    private Animation headerAnim, menuAnim, footerAnim;

    // Firebase
    private DatabaseFirestore mDatabase;

    // Custom
    private CustomDialog loading;
    private CustomToast toast;
    private CustomSound sound;

    // Data
    private String idUser, idChar, charData, wallData, decorData;
    private int levelUser;
    private ArrayList<String> inbox = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loading = new CustomDialog(HomeActivity.this);
        toast = new CustomToast(HomeActivity.this);
        sound = new CustomSound(HomeActivity.this);

        firebaseInit();

        findViewId();

        onClickSetting();

        Paper.init(HomeActivity.this);

        String character = Paper.book().read("character");
        String wallpaper = Paper.book().read("wallpaper");
        String decoration = Paper.book().read("decoration");
        String language = Paper.book().read("language");

        if (character != null && wallpaper != null && decoration != null) {
            Glide.with(HomeActivity.this).load(character).into(charView);
            Glide.with(HomeActivity.this).load(wallpaper).into(wallView);
            Glide.with(HomeActivity.this).load(decoration).into(decorView);
        }
        if (language == null) {
            Paper.book().write("language", "en");
            updateView((String) Paper.book().read("language"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            updateView((String) Paper.book().read("language"));
            loading.showLoading();
            mDatabase.getCurrentUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        idUser = document.getString("uid");
                        idChar = document.getString("idchar");
                        String email = document.getString("email");
                        charData = document.getString("usechar");
                        wallData = document.getString("usewall");
                        decorData = document.getString("usedecor");
                        levelUser = (int) (long) document.get("level");
                        int money = (int) (long) document.get("money");
                        int exp = (int) (long) document.get("exp");

                        levelTxt.setText(String.valueOf(levelUser));
                        moneyTxt.setText(String.valueOf(money));

                        int max = levelUser * 150;

                        expBar.setMax(max);
                        expBar.setProgress(exp);

                        Glide.with(HomeActivity.this).load(charData).into(charView);
                        Glide.with(HomeActivity.this).load(wallData).into(wallView);
                        Glide.with(HomeActivity.this).load(decorData).into(decorView);

                        Paper.book().write("current_user", idUser);
                        Paper.book().write("user_level", levelUser);
                        Paper.book().write("user_email", email);
                        Paper.book().write("user_levelupmax", max);
                        Paper.book().write("user_money", money);
                        Paper.book().write("user_exp", exp);
                        Paper.book().write("id_character", idChar);
                        Paper.book().write("character", charData);
                        Paper.book().write("wallpaper", wallData);
                        Paper.book().write("decoration", decorData);

                        header.setAnimation(headerAnim);
                        menu.setAnimation(menuAnim);
                        footer.setAnimation(footerAnim);
                        YoYo.with(Techniques.FadeIn).duration(1000).playOn(charView);
                        YoYo.with(Techniques.FadeIn).duration(1000).playOn(decorView);
                        if (idUser != null) {
                            checkInbox();
                        } else {
                            loading.dismissLoading();
                        }
                    } else {
                        toast.showToast("Check your connection");
                        loading.dismissLoading();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void firebaseInit() {
        mDatabase = new DatabaseFirestore();
    }

    private void findViewId() {

        header = findViewById(R.id.main_header);
        menu = findViewById(R.id.main_menu);
        footer = findViewById(R.id.main_footer);

        kanjiBtn = findViewById(R.id.main_kanji);
        phraseBtn = findViewById(R.id.main_phrase);
        grammarBtn = findViewById(R.id.main_grammar);
        profileBtn = findViewById(R.id.main_current_profile);
        settingBtn = findViewById(R.id.main_setting);
        shopBtn = findViewById(R.id.main_shop);
        itemBtn = findViewById(R.id.main_item);
        basicBtn = findViewById(R.id.main_basic);
        eventBtn = findViewById(R.id.main_event);
        rankBtn = findViewById(R.id.main_ranking);

        charView = findViewById(R.id.main_character);
        wallView = findViewById(R.id.main_wallpaper);
        decorView = findViewById(R.id.main_decoration);

        levelTxt = findViewById(R.id.level_user);
        moneyTxt = findViewById(R.id.money_user);
        expBar = findViewById(R.id.experience_user);

        headerAnim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.show_header);
        menuAnim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.show_menu);
        footerAnim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.show_footer);
    }

    private void onClickSetting() {

        kanjiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, KanjiActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("USER_LEVEL", levelUser);
                startActivity(intent);
            }
        });

        phraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, PhraseActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("USER_LEVEL", levelUser);
                startActivity(intent);
            }
        });

        grammarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, GrammarActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("USER_LEVEL", levelUser);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("CHARACTER", charData);
                intent.putExtra("ID_USER", idUser);
                startActivity(intent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("ID_USER", idUser);
                startActivity(intent);
            }
        });

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("ID_USER", idUser);
                startActivity(intent);
            }
        });

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                loading.showBasicDialog(wallData);
            }
        });

        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                loading.showEventDialog();
            }
        });

        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });

        final Techniques[] techniques = {
                Techniques.Pulse, Techniques.RubberBand, Techniques.Bounce, Techniques.Shake};

        charView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int num = random.nextInt(techniques.length);
                YoYo.with(techniques[num]).duration(1000).repeat(1).playOn(charView);
            }
        });

        decorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int num = random.nextInt(techniques.length);
                YoYo.with(techniques[num]).duration(1000).repeat(1).playOn(decorView);
            }
        });
    }

    private void updateView(String language) {
        if (language.equals("en")) {
            profileBtn.setImageResource(R.drawable.button_profile);
            settingBtn.setImageResource(R.drawable.button_setting);
            shopBtn.setImageResource(R.drawable.button_shop);
            itemBtn.setImageResource(R.drawable.button_item);
            basicBtn.setImageResource(R.drawable.button_basic);
            eventBtn.setImageResource(R.drawable.button_events);
            rankBtn.setImageResource(R.drawable.button_ranking);
        } else if (language.equals("in")) {
            profileBtn.setImageResource(R.drawable.button_profil);
            settingBtn.setImageResource(R.drawable.button_pengaturan);
            shopBtn.setImageResource(R.drawable.button_toko);
            itemBtn.setImageResource(R.drawable.button_barang);
            basicBtn.setImageResource(R.drawable.button_dasar);
            eventBtn.setImageResource(R.drawable.button_acara);
            rankBtn.setImageResource(R.drawable.button_peringkat);
        }
    }

    private void checkInbox() {
        inbox.clear();
        FirebaseFirestore.getInstance().collection("user").document(idUser)
                .collection("message").whereEqualTo("read", false)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!document.getBoolean("read")) {
                            inbox.add(document.getId());
                        }
                    }
                    if (inbox.size() != 0) {
                        TextView textView = findViewById(R.id.new_message);
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        TextView textView = findViewById(R.id.new_message);
                        textView.setVisibility(View.INVISIBLE);
                    }
                    loading.dismissLoading();
                }
            }
        });
    }
}
