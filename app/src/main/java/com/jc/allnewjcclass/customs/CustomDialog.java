package com.jc.allnewjcclass.customs;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.BasicActivity;
import com.jc.allnewjcclass.DetailActivity;
import com.jc.allnewjcclass.GrammarListActivity;
import com.jc.allnewjcclass.PhraseListActivity;
import com.jc.allnewjcclass.PracticeActivity;
import com.jc.allnewjcclass.PronunciationActivity;
import com.jc.allnewjcclass.QuizActivity;
import com.jc.allnewjcclass.R;
import com.jc.allnewjcclass.SeeProfileActivity;
import com.jc.allnewjcclass.adapters.EventSliderAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class CustomDialog {

    private CustomSound soundClick;

    private Context c;
    private Dialog loadingDialog, kanjiDialog, shopDialog, countdownDialog, pauseDialog,
            levelupDialog, resultQuizDialog, kanaDialog, rankDialog, successDialog, dangerDialog,
            itemDialog, basicDialog, eventDialog;
    private ArrayList<String> buyedItem = new ArrayList<>();

    public CustomDialog() {
    }

    public CustomDialog(Context c) {
        this.c = c;
        soundClick = new CustomSound(this.c);
        loadingDialog = new Dialog(this.c);
        kanjiDialog = new Dialog(this.c);
        shopDialog = new Dialog(this.c);
        countdownDialog = new Dialog(this.c);
        pauseDialog = new Dialog(this.c);
        levelupDialog = new Dialog(this.c);
        resultQuizDialog = new Dialog(this.c);
        kanaDialog = new Dialog(this.c);
        rankDialog = new Dialog(this.c);
        successDialog = new Dialog(this.c);
        dangerDialog = new Dialog(this.c);
        itemDialog = new Dialog(this.c);
        basicDialog = new Dialog(this.c);
        eventDialog = new Dialog(this.c);
        Paper.init(c);
    }

    public void showEventDialog() {
        eventDialog.setContentView(R.layout.dialog_event);
        eventDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ViewPager viewPager = eventDialog.findViewById(R.id.event_viewpager);
        final LinearLayout linearLayout = eventDialog.findViewById(R.id.dots);

        showLoading();
        FirebaseFirestore.getInstance().collection("event").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dismissLoading();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("GET EVENT", String.valueOf(documentSnapshot.getData()));
                                ArrayList<String> nameL = new ArrayList<>();
                                ArrayList<String> imgL = new ArrayList<>();
                                ArrayList<String> dateL = new ArrayList<>();

                                nameL.add(documentSnapshot.getString("name"));
                                imgL.add(documentSnapshot.getString("image"));
                                String date = documentSnapshot.getString("startdate")
                                        + " - " + documentSnapshot.getString("enddate");
                                dateL.add(date);

                                int position = 0;
                                final TextView[] dots;
                                EventSliderAdapter sliderAdapter;
                                sliderAdapter = new EventSliderAdapter(c, nameL, imgL, dateL);
                                viewPager.setAdapter(sliderAdapter);

                                dots = new TextView[sliderAdapter.getCount()];
                                linearLayout.removeAllViews();
                                for (int i = 0; i < dots.length; i++) {
                                    dots[i] = new TextView(c);
                                    dots[i].setText(Html.fromHtml("&#8226"));
                                    dots[i].setTextSize(35);
                                    dots[i].setTextColor(c.getResources().getColor(R.color.colorWhiteTransparent50));

                                    linearLayout.addView(dots[i]);
                                }
                                dots[position].setTextColor(c.getResources().getColor(R.color.colorWhite));

                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        linearLayout.removeAllViews();
                                        for (int i = 0; i < dots.length; i++) {
                                            dots[i] = new TextView(c);
                                            dots[i].setText(Html.fromHtml("&#8226"));
                                            dots[i].setTextSize(35);
                                            dots[i].setTextColor(c.getResources().getColor(R.color.colorWhiteTransparent50));

                                            linearLayout.addView(dots[i]);
                                        }
                                        dots[position].setTextColor(c.getResources().getColor(R.color.colorWhite));
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });
                            }
                        } else {
                            dismissLoading();
                            dismissEventDialog();
                            Toast.makeText(c, "Check your connection", Toast.LENGTH_LONG).show();
                            Log.d("ERROR EVENT", String.valueOf(task.getException()));
                        }
                    }
                });

        eventDialog.show();
    }

    public void dismissEventDialog() {
        eventDialog.dismiss();
    }

    public void showLoading() {
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.show();
    }

    public void dismissLoading() {
        loadingDialog.dismiss();
    }

    public void showDialogLevelUp() {
        levelupDialog.setContentView(R.layout.dialog_levelup);
        LottieAnimationView lottieAnimationView = levelupDialog.findViewById(R.id.level_up_lottie);
        levelupDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        levelupDialog.show();
    }

    public void dismissDialogLevelUp() {
        levelupDialog.dismiss();
    }

    public void showDialogKana(String kana, String stroke, String romaji, int animation, final int sound) {
        kanaDialog.setContentView(R.layout.dialog_detail_kana);
        SeekBar seekBar = kanaDialog.findViewById(R.id.detail_kana_animation_speed);
        final LottieAnimationView lottieAnimationView = kanaDialog.findViewById(R.id.detail_kana_animation);
        lottieAnimationView.setAnimation(animation);
        ImageView imageView = kanaDialog.findViewById(R.id.detail_kana_sound);
        TextView textViewRomaji = kanaDialog.findViewById(R.id.detail_kana_romaji);
        TextView textView1 = kanaDialog.findViewById(R.id.detail_kana1);
        TextView textView2 = kanaDialog.findViewById(R.id.detail_kana2);
        TextView textView3 = kanaDialog.findViewById(R.id.detail_kana3);
        TextView textView4 = kanaDialog.findViewById(R.id.detail_kana4);
        TextView textView5 = kanaDialog.findViewById(R.id.detail_kana5);
        TextView textViewStroke = kanaDialog.findViewById(R.id.detail_kana_stroke);
        textView1.setText(kana);
        textView2.setText(kana);
        textView3.setText(kana);
        textView4.setText(kana);
        textView5.setText(kana);
        textViewRomaji.setText(romaji);
        if (Paper.book().read("language").equals("en")) {
            textViewStroke.setText("Total Stroke : " + stroke);
        } else {
            textViewStroke.setText("Jumlah Coretan : " + stroke);
        }
        Button closeBtn = kanaDialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                dismissDialogKana();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer play = MediaPlayer.create(c, sound);
                play.setVolume(100, 100);
                play.start();
            }
        });

        seekBar.setProgress(1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lottieAnimationView.setSpeed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        kanaDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        kanaDialog.show();
    }

    public void dismissDialogKana() {
        kanaDialog.dismiss();
    }

    public void showDialogResultQuiz(String type, String question, String answer, String chosen, String correction) {
        resultQuizDialog.setContentView(R.layout.dialog_detail_quiz_result);
        ImageView questionImg = resultQuizDialog.findViewById(R.id.result_question_img);
        TextView questionTxt = resultQuizDialog.findViewById(R.id.result_question_text);
        TextView answerTxt = resultQuizDialog.findViewById(R.id.result_right_text);
        TextView chosenTxt = resultQuizDialog.findViewById(R.id.result_chosen_text);
        TextView correctionTxt = resultQuizDialog.findViewById(R.id.result_correction_text);
        Button closeBtn = resultQuizDialog.findViewById(R.id.close_btn);

        if (type.equals("image")) {
            Glide.with(c).load(question).into(questionImg);
        } else {
            questionTxt.setText("Question : " + question);
        }

        answerTxt.setText("Right answer : " + answer);
        chosenTxt.setText("Your answer : " + chosen);
        correctionTxt.setText("Correction : " + correction);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                dismissDialogResultQuiz();
            }
        });
        resultQuizDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        resultQuizDialog.show();
    }

    public void dismissDialogResultQuiz() {
        resultQuizDialog.dismiss();
    }

    public void showDialogKanji(final String id, final String wallData, final Bundle bundle) {
        kanjiDialog.setContentView(R.layout.dialog_kanji);

        Button play = kanjiDialog.findViewById(R.id.play_kanji);
        Button practice = kanjiDialog.findViewById(R.id.practice_kanji);
        Button detail = kanjiDialog.findViewById(R.id.detail_kanji);
        Button close = kanjiDialog.findViewById(R.id.close_dialog_kanji);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, QuizActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", bundle);
                c.startActivity(intent);
                dismissDialogKanji();
            }
        });

        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, PracticeActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", bundle);
                c.startActivity(intent);
                dismissDialogKanji();
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, DetailActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", bundle);
                c.startActivity(intent);
                dismissDialogKanji();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                dismissDialogKanji();
            }
        });
        practice.setVisibility(View.GONE);
        kanjiDialog.setCanceledOnTouchOutside(false);
        kanjiDialog.setCancelable(false);
        kanjiDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        kanjiDialog.show();
    }

    public void dismissDialogKanji() {
        kanjiDialog.dismiss();
    }

    public void showDialogShopBuy(final String idUser, final String idItem, final String collection,
                                  final Map map, final int price, final int money) {
        shopDialog.setContentView(R.layout.dialog_detail_shop);

        ImageView thumbImg = shopDialog.findViewById(R.id.shop_buy_image);
        ImageView fullImg = shopDialog.findViewById(R.id.shop_buy_full);
        TextView japanTxt = shopDialog.findViewById(R.id.shop_buy_japanname);
        TextView nameTxt = shopDialog.findViewById(R.id.shop_buy_name);
        TextView priceTxt = shopDialog.findViewById(R.id.shop_buy_price);
        TextView effectTxt = shopDialog.findViewById(R.id.shop_buy_effect);
        Button close = shopDialog.findViewById(R.id.shop_buy_close_btn);
        Button buy = shopDialog.findViewById(R.id.shop_buy_btn);

        Glide.with(this.c).load(map.get("thumbimg").toString()).into(thumbImg);
        Glide.with(this.c).load(map.get("fullimg").toString()).into(fullImg);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        fullImg.setColorFilter(filter);
        japanTxt.setText(map.get("japanname").toString());
        nameTxt.setText(map.get("name").toString());
        priceTxt.setText(String.valueOf(price));
        String effect = "Exp x" + map.get("exp") + " | " + "Money x" + map.get("money");
        effectTxt.setText(effect);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                dismissDialogShopBuy();
            }
        });

        final String lang = Paper.book().read("language");
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                if (money < price) {
                    if (lang.equals("in")) {
                        Toast.makeText(c, "Uang tidak cukup", Toast.LENGTH_SHORT).show();
                    } else if (lang.equals("en")) {
                        Toast.makeText(c, "Insufficient funds", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showLoading();
                    FirebaseFirestore.getInstance().collection("user")
                            .document(idUser).collection(collection).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            buyedItem.add(document.getId());
                                        }
                                        if (buyedItem.contains(idItem)) {
                                            showDanger();
                                            dismissDialogShopBuy();
                                            dismissLoading();
                                        } else {
                                            FirebaseFirestore.getInstance().collection("user")
                                                    .document(idUser).collection(collection).document(idItem)
                                                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    int newMoney = money - price;
                                                    Map<String, Object> moneyMap = new HashMap<>();
                                                    moneyMap.put("money", newMoney);
                                                    FirebaseFirestore.getInstance().collection("user")
                                                            .document(idUser).update(moneyMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    showSuccess();
                                                                    dismissDialogShopBuy();
                                                                    dismissLoading();
                                                                }
                                                            });
                                                }
                                            });
                                        }
                                    } else {
                                        if (lang.equals("in")) {
                                            Toast.makeText(c, "Ada yang tidak beres, coba lagi nanti.", Toast.LENGTH_SHORT).show();
                                        } else if (lang.equals("en")) {
                                            Toast.makeText(c, "Something error, try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                        showDanger();
                                        dismissDialogShopBuy();
                                        dismissLoading();
                                    }
                                }
                            });
                }
            }
        });

        shopDialog.setCanceledOnTouchOutside(false);
        shopDialog.setCancelable(false);
        shopDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        shopDialog.show();
    }

    public void dismissDialogShopBuy() {
        shopDialog.dismiss();
    }

    public LottieAnimationView showCountdown(Boolean start) {
        countdownDialog.setContentView(R.layout.dialog_countdown);

        String lang = Paper.book().read("language");

        RelativeLayout relativeLayout = countdownDialog.findViewById(R.id.start_quiz);
        final TextView textView = countdownDialog.findViewById(R.id.touch_text);
        final LottieAnimationView lottieAnimationView = countdownDialog.findViewById(R.id.countdown_lottie);

        if (lang.equals("en")) {
            textView.setText("Touch to start");
        } else {
            textView.setText("Sentuh untuk mulai");
        }

        if (!start) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setVisibility(View.INVISIBLE);
                    lottieAnimationView.playAnimation();

                    lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dismissCountdown();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            });
        }

        countdownDialog.setCanceledOnTouchOutside(false);
        countdownDialog.setCancelable(false);
        countdownDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        countdownDialog.show();

        return lottieAnimationView;
    }

    public void dismissCountdown() {
        countdownDialog.dismiss();
    }

    public void showPauseQuiz(final String id,
                              final String wallData,
                              final Bundle bundle,
                              final CountdownView countdownView,
                              final LinearLayout linearLayout) {
        pauseDialog.setContentView(R.layout.dialog_pause);

        Button resumeBtn = pauseDialog.findViewById(R.id.resume_quiz_btn);
        Button restartBtn = pauseDialog.findViewById(R.id.restart_quiz_btn);
        Button quitBtn = pauseDialog.findViewById(R.id.quit_quiz_btn);

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                dismsissPauseQuiz();
                linearLayout.setVisibility(View.VISIBLE);
//                countdownView.start(countdownMillis);
                countdownView.restart();
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                ((Activity) c).finish();
                Intent intent = ((Activity) c).getIntent();
                intent.putExtra("ID", id);
                intent.putExtra("WALLPAPER", wallData);
                intent.putExtra("BUNDLE", bundle);
                c.startActivity(intent);
                dismsissPauseQuiz();
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                ((Activity) c).finish();
            }
        });

        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.setCancelable(false);
        pauseDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pauseDialog.show();
    }

    public void dismsissPauseQuiz() {
        pauseDialog.dismiss();
    }

    public void showSeeProfileDialog(final String id, String name, String avatar, String kanji, String phrases, String grammar) {
        rankDialog.setContentView(R.layout.dialog_rank_list);
        TextView textViewName = rankDialog.findViewById(R.id.dialog_see_name);
        CircleImageView circleImageView = rankDialog.findViewById(R.id.dialog_see_avatar);
        TextView textViewKanji = rankDialog.findViewById(R.id.dialog_see_kanji);
        TextView textViewPhrases = rankDialog.findViewById(R.id.dialog_see_phrases);
        TextView textViewKGrammar = rankDialog.findViewById(R.id.dialog_see_grammar);
        Button buttonSeeProfile = rankDialog.findViewById(R.id.dialog_see_profile);
        Button buttonSeeClose = rankDialog.findViewById(R.id.dialog_see_profile_close);

        textViewName.setText(name);
        textViewKanji.setText(kanji);
        textViewPhrases.setText(phrases);
        textViewKGrammar.setText(grammar);

        if (!avatar.equals("default")) {
            Glide.with(c).load(avatar).into(circleImageView);
        }

        buttonSeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, SeeProfileActivity.class);
                intent.putExtra("ID_USER", id);
                c.startActivity(intent);
            }
        });

        buttonSeeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                rankDialog.dismiss();
            }
        });

        rankDialog.setCanceledOnTouchOutside(false);
        rankDialog.setCancelable(false);
        rankDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        rankDialog.show();
    }

    public void showSuccess() {
        successDialog.setContentView(R.layout.dialog_success);
        successDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        successDialog.show();
    }

    public void showBasicDialog(final String wallData) {
        basicDialog.setContentView(R.layout.dialog_basic);
        Button kana = basicDialog.findViewById(R.id.dialog_basic_kana);
        Button pronun = basicDialog.findViewById(R.id.dialog_basic_pronoun);
        Button phrases = basicDialog.findViewById(R.id.dialog_basic_phrases);
        Button particles = basicDialog.findViewById(R.id.dialog_basic_particles);
        TextView textView = basicDialog.findViewById(R.id.dialog_basic_close);
        kana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, BasicActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                c.startActivity(intent);
                basicDialog.dismiss();
            }
        });
        pronun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, PronunciationActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                c.startActivity(intent);
                basicDialog.dismiss();
            }
        });
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, PhraseListActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                c.startActivity(intent);
                basicDialog.dismiss();
            }
        });
        particles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Intent intent = new Intent(c, GrammarListActivity.class);
                intent.putExtra("WALLPAPER", wallData);
                c.startActivity(intent);
                basicDialog.dismiss();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicDialog.dismiss();
            }
        });
        basicDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        basicDialog.show();
    }

    public void showDanger() {
        dangerDialog.setContentView(R.layout.dialog_danger);
        dangerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dangerDialog.show();
    }

    public void showItem(final String collection, final String idUser, final String id, String japan, String name, String exp, String money,
                         String desEN, String desIN, String desJP, final String img) {
        itemDialog.setContentView(R.layout.dialog_item);
        TextView japanTxt = itemDialog.findViewById(R.id.dialog_item_japanname);
        TextView nameTxt = itemDialog.findViewById(R.id.dialog_item_name);
        TextView expTxt = itemDialog.findViewById(R.id.dialog_item_exp);
        TextView moneyTxt = itemDialog.findViewById(R.id.dialog_item_money);
        TextView desTxt = itemDialog.findViewById(R.id.dialog_item_description);
        TextView desJPTxt = itemDialog.findViewById(R.id.dialog_item_description_JP);
        Button useBtn = itemDialog.findViewById(R.id.dialog_item_use);
        ImageView imageView = itemDialog.findViewById(R.id.dialog_item_img);
        Glide.with(c).load(img).into(imageView);
        desTxt.setMovementMethod(new ScrollingMovementMethod());
        desJPTxt.setMovementMethod(new ScrollingMovementMethod());

        String lang = Paper.book().read("language");
        if (lang.equals("en")) {
            japanTxt.setText("Japanese Name : " + japan);
            nameTxt.setText("Name : " + name);
            if (!exp.equals("0") && !money.equals("0") && desEN != null && desJP != null) {
                expTxt.setText("Exp Multiplier : " + exp);
                moneyTxt.setText("Money Multiplier : " + money);
                desTxt.setText("Description : " + desEN);
                desJPTxt.setText("Description in Japanese : " + desJP);
            }
            useBtn.setText("Use");
        } else if (lang.equals("in")) {
            japanTxt.setText("Nama Jepang : " + japan);
            nameTxt.setText("Nama : " + name);
            if (!exp.equals("0") && !money.equals("0") && desIN != null && desJP != null) {
                expTxt.setText("Pengali Exp : " + exp);
                moneyTxt.setText("Pengali Uang : " + money);
                desTxt.setText("Deskripsi : " + desIN);
                desJPTxt.setText("Deskripsi bahasa Jepang : " + desJP);
            }
            useBtn.setText("Gunakan");
        }
        useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundClick.playBtnClickSound();
                Map<String, Object> map = new HashMap<>();
                if (collection.equals("character")) {
                    map.put("idchar", id);
                    map.put("usechar", img);
                } else if (collection.equals("wallpaper")) {
                    map.put("usewall", img);
                } else if (collection.equals("decoration")) {
                    map.put("usedecor", img);
                }
                showLoading();
                FirebaseFirestore.getInstance().collection("user").document(idUser)
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            itemDialog.dismiss();
                            dismissLoading();
                            showSuccess();
                        } else {
                            itemDialog.dismiss();
                            dismissLoading();
                            showDanger();
                        }
                    }
                });
            }
        });
        itemDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        itemDialog.show();
    }
}
