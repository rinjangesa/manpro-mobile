package com.jc.allnewjcclass;

import android.animation.Animator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;

public class EndlessActivity extends AppCompatActivity {

    // UI
    private ImageView wallView, questionImg;
    private TextView questionTxt,
            option1Txt,
            option2Txt,
            option3Txt,
            option4Txt,
            countdownPlusTxt,
            scoreTxt,
            correctionTxt;
    private CardView option1Card, option2Card, option3Card, option4Card;
    private LinearLayout quizLayout;
    private CountdownView countdownView;
    private Animation fadeOutAnim;
    private Button pauseBtn;

    // Firebase
    private FirebaseFirestore mStore;
    private DatabaseFirestore databaseFirestore;
    // Custom
    private CustomDialog dialog;
    private CustomToast toast;
    private CustomSound sound;

    // Data
    private int score;
    private int quizCount = 0;
    static private int QUIZ_COUNT;
    private String
            collection,
            wallData,
            questionType,
            idQuestion,
            question,
            rightAnswer,
            correction;
    private ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    private ArrayList<Map> resultList = new ArrayList<>();

    // Timer
    private long countdown_millis = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endless);

        collection = getIntent().getStringExtra("COLLECTION");
        wallData = getIntent().getStringExtra("WALLPAPER");

        dialog = new CustomDialog(EndlessActivity.this);
        toast = new CustomToast(EndlessActivity.this);
        sound = new CustomSound(EndlessActivity.this);
        databaseFirestore = new DatabaseFirestore();

        firebaseInit();

        findViewId();

        onClickSetting();

        Glide.with(EndlessActivity.this).load(wallData).into(wallView);

        dialog.showLoading();
        mStore.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QUIZ_COUNT = task.getResult().size() - 1;

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            ArrayList<String> tmpArray = new ArrayList<>();
                            tmpArray.add(document.getString("answer")); // index 0
                            tmpArray.add(document.getString("option1")); // index 1
                            tmpArray.add(document.getString("option2")); // index 2
                            tmpArray.add(document.getString("option3")); // index 3
                            tmpArray.add(document.getString("question")); // index 4
                            tmpArray.add(document.getString("id")); // index 5
                            tmpArray.add(document.getString("type")); // index 6
                            tmpArray.add(document.getString("correction")); // index 7
                            quizArray.add(tmpArray);
                        } else {
                            finish();
                        }
                    }
                    showNextQuiz();
                    dialog.dismissLoading();
                    dialog.showCountdown(false).addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            stopService(new Intent(EndlessActivity.this, CustomSound.class));
                            sound.playCountdownSound();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            quizLayout.setVisibility(View.VISIBLE);
                            countdownView.start(countdown_millis);
                            countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                                @Override
                                public void onEnd(CountdownView cv) {
                                    showResult();
                                }
                            });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void firebaseInit() {
        mStore = FirebaseFirestore.getInstance();
    }

    private void findViewId() {
        wallView = findViewById(R.id.endless_wallpaper);
        countdownView = findViewById(R.id.countdown_timer);
        countdownPlusTxt = findViewById(R.id.countdown_timer_plus);
        pauseBtn = findViewById(R.id.pause_btn);
        scoreTxt = findViewById(R.id.score_text);
        questionTxt = findViewById(R.id.question);
        questionImg = findViewById(R.id.question_image);
        option1Txt = findViewById(R.id.option_1);
        option2Txt = findViewById(R.id.option_2);
        option3Txt = findViewById(R.id.option_3);
        option4Txt = findViewById(R.id.option_4);
        option1Card = findViewById(R.id.option_1_container);
        option2Card = findViewById(R.id.option_2_container);
        option3Card = findViewById(R.id.option_3_container);
        option4Card = findViewById(R.id.option_4_container);
        correctionTxt = findViewById(R.id.endless_correction_text);
        quizLayout = findViewById(R.id.layout_quiz);
        quizLayout.setVisibility(View.INVISIBLE);

        fadeOutAnim = AnimationUtils.loadAnimation(EndlessActivity.this, R.anim.fade_out);
        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                countdownPlusTxt.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onClickSetting() {
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownView.pause();
                quizLayout.setVisibility(View.INVISIBLE);
                dialog.showPauseQuiz("", wallData, new Bundle(), countdownView, quizLayout);
            }
        });
    }

    private void showNextQuiz() {

        resetLayout();

        //Membuat urutan acak
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quizList = quizArray.get(randomNum);
        rightAnswer = quizList.get(0);
        correction = quizList.get(7);
        questionType = quizList.get(6);
        idQuestion = quizList.get(5);
        question = quizList.get(4);

        //Mengatur pertanyaan dan tipe soal
        if (quizList.get(6).equals("text") || quizList.get(6).equals("boolean")) {
            questionTxt.setVisibility(View.VISIBLE);
            questionImg.setVisibility(View.INVISIBLE);
            questionTxt.setText(quizList.get(4));
        } else if (quizList.get(6).equals("image")) {
            questionTxt.setVisibility(View.INVISIBLE);
            questionImg.setVisibility(View.VISIBLE);
            Glide.with(EndlessActivity.this).load(quizList.get(4)).into(questionImg);
        }

        // Menentukan pilihan dari tipe soal
        if (quizList.get(6).equals("text") || quizList.get(6).equals("image")) {

            //Melepas array yg tidak digunakan
            quizList.remove(7); // remove correction
            quizList.remove(6); // remove type
            quizList.remove(5); // remove id
            quizList.remove(4); // remove question

            //Mengacak pilihan
            Collections.shuffle(quizList);

            String a = "A. " + quizList.get(0);
            String b = "B. " + quizList.get(1);
            String c = "C. " + quizList.get(2);
            String d = "D. " + quizList.get(3);

            option1Txt.setText(a);
            option2Txt.setText(b);
            option3Txt.setText(c);
            option4Txt.setText(d);
            option1Card.setContentDescription(quizList.get(0));
            option2Card.setContentDescription(quizList.get(1));
            option3Card.setContentDescription(quizList.get(2));
            option4Card.setContentDescription(quizList.get(3));

        } else if (quizList.get(6).equals("boolean")) {
            //Melepas array yg tidak digunakan
            quizList.remove(7);
            quizList.remove(6);
            quizList.remove(5);
            quizList.remove(4);
            quizList.remove(3);
            quizList.remove(2);
            quizList.remove(1);

            option1Txt.setText(R.string.trues);
            option2Txt.setText(R.string.falses);
            option3Txt.setVisibility(View.INVISIBLE);
            option4Txt.setVisibility(View.INVISIBLE);

            if (quizList.get(0).equals("true")) {
                option1Card.setContentDescription(quizList.get(0));
                option2Card.setContentDescription("false");
            } else {
                option1Card.setContentDescription("true");
                option2Card.setContentDescription(quizList.get(0));
            }
            option3Card.setVisibility(View.INVISIBLE);
            option4Card.setVisibility(View.INVISIBLE);
        }

        //Lepas urutan acak yg sudah
        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view) {

        CardView chosenView = findViewById(view.getId());
        String chosenAnswer = (String) chosenView.getContentDescription();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("QUESTION_ID", idQuestion);
        resultMap.put("QUESTION_TYPE", questionType);
        resultMap.put("QUESTION", question);
        resultMap.put("CORRECTION", correction);
        resultMap.put("RIGHT_ANSWER", rightAnswer);
        resultMap.put("CHOSEN_ANSWER", chosenAnswer);

        //Kondisi koreksi button
        if (chosenAnswer.equals(rightAnswer)) {
            sound.playCorrectSound();
            score++;
            countdown_millis = countdown_millis + 5000;
            countdownView.start(countdown_millis);
            countdownPlusTxt.setText("+5");
            countdownPlusTxt.setVisibility(View.VISIBLE);
            countdownPlusTxt.setAnimation(fadeOutAnim);
            countdownPlusTxt.startAnimation(fadeOutAnim);

            String scores = "SCORE \n" + score;

            scoreTxt.setText(scores);
            resultMap.put("CORRECT", "correct");
        } else {
            sound.playWrongSound();
            countdown_millis = countdown_millis - 2000;
            countdownView.start(countdown_millis);
            countdownPlusTxt.setText("-2");
            countdownPlusTxt.setVisibility(View.VISIBLE);
            countdownPlusTxt.setAnimation(fadeOutAnim);
            countdownPlusTxt.startAnimation(fadeOutAnim);
            resultMap.put("CORRECT", "wrong");
        }

        resultList.add(resultMap);

        //batas soal
        if (quizCount == QUIZ_COUNT) {
            //show result
            showResult();
        } else {
            quizCount++;
            showNextQuiz();
        }

        correctionTxt.setVisibility(View.VISIBLE);
        String correctionBefore = resultList.get(resultList.size() - 1).get("CORRECTION").toString();
        correctionTxt.setText(correctionBefore);
    }

    private void showResult() {
        finish();
        Intent intent = new Intent(EndlessActivity.this, QuizResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("ID_USER", databaseFirestore.getUID());
        intent.putExtra("WALLPAPER", wallData);
        intent.putExtra("HISTORY", resultList);
        intent.putExtra("ENDLESS", "endless_" + collection);
        startActivity(intent);
    }

    private void resetLayout() {
        option1Txt.setVisibility(View.VISIBLE);
        option2Txt.setVisibility(View.VISIBLE);
        option3Txt.setVisibility(View.VISIBLE);
        option4Txt.setVisibility(View.VISIBLE);
        option1Card.setVisibility(View.VISIBLE);
        option2Card.setVisibility(View.VISIBLE);
        option3Card.setVisibility(View.VISIBLE);
        option4Card.setVisibility(View.VISIBLE);
        correctionTxt.setVisibility(View.INVISIBLE);
    }

}
