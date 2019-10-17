package com.jc.allnewjcclass;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.helpers.LocaleHelper;

import io.paperdb.Paper;

public class SettingActivity extends AppCompatActivity {

    // UI
    private ImageButton profileBtn, homeBtn;
    private Button helpBtn, logoutBtn, changePassBtn;
    private ImageView wallView;
    private CheckBox muteCheckbox;
    private TextView changeLangTxt, muteTxt, soundTxt;
    private Button enRadio, inRadio;
    private SeekBar volumeBar;

    // Firebase
    private FirebaseAuth mAuth;

    // Custom
    private CustomSound sound;
    private CustomToast toast;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Paper.init(SettingActivity.this);

        sound = new CustomSound(SettingActivity.this);
        toast = new CustomToast(SettingActivity.this);
        dialog = new CustomDialog(SettingActivity.this);

        String wallData = getIntent().getStringExtra("WALLPAPER");

        firebaseInit();

        findViewId();

        onClickSetting();

        Glide.with(SettingActivity.this).load(wallData).into(wallView);

        Paper.init(SettingActivity.this);
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language", "en");

        updateView((String) Paper.book().read("language"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Paper.book().read("language").equals("en")) {
            enRadio.setEnabled(false);
            inRadio.setEnabled(true);
        } else if (Paper.book().read("language").equals("in")) {
            enRadio.setEnabled(true);
            inRadio.setEnabled(false);
        }
    }

    private void findViewId() {
        logoutBtn = findViewById(R.id.setting_btn_logout);
        changePassBtn = findViewById(R.id.setting_btn_change_password);
        helpBtn = findViewById(R.id.setting_btn_help);
        profileBtn = findViewById(R.id.setting_current_profile);
        homeBtn = findViewById(R.id.btn_home);
        wallView = findViewById(R.id.setting_wallpaper);
        muteCheckbox = findViewById(R.id.setting_mute);
        changeLangTxt = findViewById(R.id.change_lang);
        enRadio = findViewById(R.id.radio_english);
        inRadio = findViewById(R.id.radio_indonesia);
        muteTxt = findViewById(R.id.setting_mute_label);
        soundTxt = findViewById(R.id.setting_bgm_label);
        volumeBar = findViewById(R.id.setting_bgm_bar);
        volumeBar.setVisibility(View.INVISIBLE);
        soundTxt.setVisibility(View.INVISIBLE);

        String mute = Paper.book().read("MUTE");
        if (mute != null && mute.equals("ON")) {
            muteCheckbox.setChecked(true);
        } else {
            muteCheckbox.setChecked(false);
        }
    }

    private void onClickSetting() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                mAuth.signOut();
                finish();
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();

            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                dialog.showLoading();
                String email = Paper.book().read("user_email");
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismissLoading();
                                    toast.showToast("Sent, check your email.");
                                } else {
                                    dialog.dismissLoading();
                                    toast.showToast("Failed sending.");
                                }
                            }
                        });
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
    }

    private void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void onCheckboxClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.setting_mute:
                if (checked) {
                    stopService(new Intent(SettingActivity.this, CustomSound.class));
                    Paper.book().write("MUTE", "ON");
                } else {
                    startService(new Intent(SettingActivity.this, CustomSound.class));
                    Paper.book().write("MUTE", "OFF");
                }
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((Button) view).isEnabled();

        switch (view.getId()) {
            case R.id.radio_english:
                if (checked)
                    inRadio.setEnabled(true);
                view.setEnabled(false);
                Paper.book().write("language", "en");
                updateView((String) Paper.book().read("language"));
                break;
            case R.id.radio_indonesia:
                if (checked)
                    enRadio.setEnabled(true);
                view.setEnabled(false);
                Paper.book().write("language", "in");
                updateView((String) Paper.book().read("language"));
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(SettingActivity.this, language);
        Resources resources = context.getResources();

        changeLangTxt.setText(resources.getString(R.string.language));
        enRadio.setText(resources.getString(R.string.english));
        inRadio.setText(resources.getString(R.string.indonesia));
        muteTxt.setText(resources.getString(R.string.mute));
        soundTxt.setText(resources.getString(R.string.sound));
        helpBtn.setText(resources.getString(R.string.help));
        changePassBtn.setText(resources.getString(R.string.changepass));
        logoutBtn.setText(resources.getString(R.string.logout));

        if (language.equals("en")) {
            homeBtn.setImageResource(R.drawable.button_home);
        } else {
            homeBtn.setImageResource(R.drawable.button_beranda);
        }
    }
}
