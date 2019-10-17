package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.adapters.ResultQuizAdapter;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class QuizResultActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private RecyclerView recyclerView;
    private TextView scoreLabel, scoreTxt, moneyTxt, expTxt;
    private Button homeBtn, categoryBtn, selectedCategBtn, tryAgainBtn;

    // customs
    private CustomDialog dialog;

    // Firebase
    private DatabaseFirestore databaseFirestore;

    // Data
    private String
            idUser,
            idKanji,
            wallData,
            scoreData,
            endless;
    private ArrayList<Map> resultList;
    private int score;
    private int exp_x;
    private int money_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        Paper.init(QuizResultActivity.this);

        idUser = getIntent().getStringExtra("ID_USER");
        idKanji = getIntent().getStringExtra("ID_KANJI");
        wallData = getIntent().getStringExtra("WALLPAPER");
        score = getIntent().getIntExtra("SCORE", 0);
        scoreData = String.valueOf(getIntent().getIntExtra("SCORE", 0));
        resultList = (ArrayList<Map>) getIntent().getSerializableExtra("HISTORY");
        endless = getIntent().getStringExtra("ENDLESS");

        dialog = new CustomDialog(QuizResultActivity.this);
        databaseFirestore = new DatabaseFirestore();

        findViewId();

        onClickSetting();

        scoreTxt.setText(scoreData);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuizResultActivity.this));
        recyclerView.setAdapter(new ResultQuizAdapter(QuizResultActivity.this, resultList));

        Glide.with(QuizResultActivity.this).load(wallData).into(wallView);
        if (endless.equals("endless_kanji_quiz")) {
            updateBestScore(idUser, "bestkanji", "lastkanji", score);
        } else if (endless.equals("endless_phrase_quiz")) {
            updateBestScore(idUser, "bestphrases", "lastphrases", score);
        } else if (endless.equals("endless_grammar_quiz")) {
            updateBestScore(idUser, "bestgrammar", "lastgrammar", score);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mute = Paper.book().read("MUTE");
        if (mute != null && mute.equals("OFF")) {
            startService(new Intent(QuizResultActivity.this, CustomSound.class));
        } else if (mute != null && mute.equals("ON")) {
            stopService(new Intent(QuizResultActivity.this, CustomSound.class));
        }
        dialog.showLoading();
        String idChar = Paper.book().read("id_character");
        databaseFirestore.getCurrentChatData(idChar).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int getResultMoney = (int) (long) document.get("money") * score;
                    int getResultExp = (int) (long) document.get("exp") * score;

                    moneyTxt.setText(String.valueOf(getResultMoney));
                    expTxt.setText(String.valueOf(getResultExp));

                    final int moneyNow = getResultMoney + Paper.book().read("user_money", 0);
                    final int expNow = getResultExp + Paper.book().read("user_exp", 0);
                    final int levelUser = Paper.book().read("user_level", 0);
                    final int max = levelUser * 150;

                    if (max <= expNow) {
                        dialog.showDialogLevelUp();
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("money", moneyNow);
                        userMap.put("exp", expNow);
                        userMap.put("level", levelUser + 1);
                        databaseFirestore.updateUser(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Paper.book().write("user_money", moneyNow);
                                    Paper.book().write("user_exp", expNow);
                                    Paper.book().write("user_level", levelUser);
                                    dialog.dismissLoading();

                                }
                            }
                        });
                    } else {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("money", moneyNow);
                        userMap.put("exp", expNow);
                        userMap.put("level", levelUser);
                        databaseFirestore.updateUser(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Paper.book().write("user_money", moneyNow);
                                    Paper.book().write("user_exp", expNow);
                                    Paper.book().write("user_level", levelUser);
                                    dialog.dismissLoading();
                                } else {
                                    dialog.dismissLoading();
                                }
                            }
                        });
                    }
                } else {
                    dialog.dismissLoading();
                }
            }
        });
    }

    private void findViewId() {
        wallView = findViewById(R.id.result_wallpaper);
        recyclerView = findViewById(R.id.list_result_quiz);
        scoreLabel = findViewById(R.id.result_score_label);
        scoreTxt = findViewById(R.id.result_score_txt);
        moneyTxt = findViewById(R.id.result_money_txt);
        expTxt = findViewById(R.id.result_exp_txt);
        homeBtn = findViewById(R.id.btn_home);
        categoryBtn = findViewById(R.id.btn_category);
        selectedCategBtn = findViewById(R.id.btn_selected_category);
        tryAgainBtn = findViewById(R.id.btn_try_again);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), KanjiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        selectedCategBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void updateBestScore(String uid, final String bestScoreField, final String lastScoreField, final int score) {
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("score").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    int bestScore = (int) (long) documentSnapshot.get(bestScoreField);
                    if (score > bestScore) {
                        Map<String, Object> scoreMap = new HashMap<>();
                        scoreMap.put(bestScoreField, score);
                        scoreMap.put(lastScoreField, score);
                        documentReference.update(scoreMap);
                    } else {
                        Map<String, Object> scoreMap = new HashMap<>();
                        scoreMap.put(lastScoreField, score);
                        documentReference.update(scoreMap);
                    }
                }
            }
        });
    }
}
