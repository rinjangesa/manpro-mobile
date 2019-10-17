package com.jc.allnewjcclass;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Message;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;
import com.jc.allnewjcclass.helpers.LocaleHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    // UI
    private ImageButton homeBtn, settingBtn;
    private ImageView charView, wallView;
    private CircleImageView avatarView;
    private TextView titleTxt, nameTxt, statusTxt, lastKanji, bestKanji, lastPhrases, bestPhrases, lastGrammar, bestGrammar;
    private RecyclerView recyclerView;
    private Button editBtn;

    // Firebase
    private DatabaseFirestore mDatabase;
    private FirestoreRecyclerAdapter adapter;

    // Customs
    private CustomDialog dialog;
    private CustomToast toast;
    private CustomSound sound;

    // Data
    private String idUser, wallData;
    private String nickname, title, avatar, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Paper.init(ProfileActivity.this);

        idUser = getIntent().getStringExtra("ID_USER");
        String charData = getIntent().getStringExtra("CHARACTER");
        wallData = getIntent().getStringExtra("WALLPAPER");

        dialog = new CustomDialog(ProfileActivity.this);
        toast = new CustomToast(ProfileActivity.this);
        sound = new CustomSound(ProfileActivity.this);

        firebaseInit();

        findViewId();

        onClickSetting();

        Glide.with(ProfileActivity.this).load(charData).into(charView);
        Glide.with(ProfileActivity.this).load(wallData).into(wallView);

        if (idUser == null) {
            idUser = Paper.book().read("current_user");
        }

        firebaseRecycler(idUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView((String) Paper.book().read("language"));
        adapter.startListening();
        dialog.showLoading();
        mDatabase.getCurrentUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();

                    final String uid = document.getString("uid");
                    nickname = document.getString("nickname");
                    title = document.getString("title");
                    avatar = document.getString("avatar");
                    status = document.getString("status");

                    if (avatar != null && !avatar.equals("default")) {
                        Glide.with(ProfileActivity.this).load(avatar).into(avatarView);
                    }

                    titleTxt.setText(title);
                    nameTxt.setText(nickname);
                    statusTxt.setText(status);
                    editBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sound.playBtnClickSound();
                            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            intent.putExtra("ID_USER", idUser);
                            intent.putExtra("WALLPAPER", wallData);
                            intent.putExtra("NICKNAME", nickname);
                            intent.putExtra("TITLE", title);
                            intent.putExtra("AVATAR", avatar);
                            intent.putExtra("STATUS", status);
                            startActivity(intent);
                        }
                    });
                    FirebaseFirestore.getInstance().collection("score")
                            .document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String lastkanjiT = "Last " + documentSnapshot.get("lastkanji");
                                String bestkanjiT = "Best " + documentSnapshot.get("bestkanji");
                                String lastphrasesT = "Last " + documentSnapshot.get("lastphrases");
                                String bestphrasesT = "Best " + documentSnapshot.get("bestphrases");
                                String lastgrammarT = "Last " + documentSnapshot.get("lastgrammar");
                                String bestgrammarT = "Best " + documentSnapshot.get("bestgrammar");
                                lastKanji.setText(lastkanjiT);
                                bestKanji.setText(bestkanjiT);
                                lastPhrases.setText(lastphrasesT);
                                bestPhrases.setText(bestphrasesT);
                                lastGrammar.setText(lastgrammarT);
                                bestGrammar.setText(bestgrammarT);
                                dialog.dismissLoading();
                            } else {
                                dialog.dismissLoading();
                            }
                        }
                    });
                    FirebaseFirestore.getInstance().collection("user")
                            .document(uid).collection("message").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            FirebaseFirestore.getInstance()
                                                    .collection("user")
                                                    .document(uid).collection("message")
                                                    .document(documentSnapshot.getId())
                                                    .update("read", true);
                                        }
                                    }
                                }
                            });
                } else {
                    toast.showToast("Check your connection");
                    dialog.dismissLoading();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void firebaseInit() {
        mDatabase = new DatabaseFirestore();
    }

    private void findViewId() {
        settingBtn = findViewById(R.id.profile_setting);
        homeBtn = findViewById(R.id.btn_home);
        charView = findViewById(R.id.profile_char_close_up);
        wallView = findViewById(R.id.profile_wallpaper);
        avatarView = findViewById(R.id.profile_avatar);
        titleTxt = findViewById(R.id.profile_title);
        nameTxt = findViewById(R.id.profile_username);
        statusTxt = findViewById(R.id.profile_status);
        lastKanji = findViewById(R.id.kanji_last_score);
        bestKanji = findViewById(R.id.kanji_best_score);
        lastPhrases = findViewById(R.id.phrase_last_score);
        bestPhrases = findViewById(R.id.phrase_best_score);
        lastGrammar = findViewById(R.id.grammar_last_score);
        bestGrammar = findViewById(R.id.grammar_best_score);
        recyclerView = findViewById(R.id.list_profile_message);
        editBtn = findViewById(R.id.profile_edit_btn);
    }

    private void onClickSetting() {
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(intent);
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

    private void firebaseRecycler(String uid) {
        Query query = FirebaseFirestore.getInstance()
                .collection("user")
                .document(idUser)
                .collection("message")
                .orderBy("sentdate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Message, ProfileActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull final ProfileActivity.ViewHolder holder, int position, @NonNull Message model) {

                final String from = model.getFrom();
                String message = model.getMessage();
                Date sentdate = model.getSentdate();

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                String date = df.format(sentdate);

                holder.setDate(date);
                holder.setMessage(message);
                if (!from.equals(idUser)){
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sound.playBtnClickSound();
                            Intent intent = new Intent(ProfileActivity.this, SeeProfileActivity.class);
                            intent.putExtra("ID_USER", from);
                            startActivity(intent);
                        }
                    });
                }

                FirebaseFirestore.getInstance().collection("user")
                        .document(from).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            holder.setName(document.getString("nickname"));
                            holder.setAva(document.getString("avatar"));
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProfileActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_message, group, false);

                return new ProfileActivity.ViewHolder(view);
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setAva(String string) {
            ImageView imageView = itemView.findViewById(R.id.single_message_ava);
            if (!string.equals("default")) {
                Glide.with(ProfileActivity.this).load(string).into(imageView);
            }
        }

        void setName(String string) {
            TextView textView = itemView.findViewById(R.id.single_message_name);
            textView.setText(string);
        }

        void setMessage(String string) {
            TextView textView = itemView.findViewById(R.id.single_message_content);
            textView.setText(string);
        }

        void setDate(String string) {
            TextView textView = itemView.findViewById(R.id.single_message_date);
            textView.setText(string);
        }
    }

    private void updateView(String language) {
        if (language.equals("en")) {
//
        } else if (language.equals("in")) {
//
        }
    }
}
