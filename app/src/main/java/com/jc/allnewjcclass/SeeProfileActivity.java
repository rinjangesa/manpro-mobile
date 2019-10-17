package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Message;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SeeProfileActivity extends AppCompatActivity {

    // UI
    private ImageButton homeBtn, settingBtn;
    private ImageView charView, wallView;
    private CircleImageView avatarView;
    private TextView titleTxt, nameTxt, statusTxt, lastKanji, bestKanji, lastPhrases, bestPhrases, lastGrammar, bestGrammar;
    private RecyclerView recyclerView;
    private EditText inputMessageText;
    private Button sendBtn;


    // Firebase
    private DatabaseFirestore mDatabase;
    private FirestoreRecyclerAdapter adapter;

    // Customs
    private CustomDialog dialog;
    private CustomToast toast;

    // Data
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);
        Paper.init(SeeProfileActivity.this);

        idUser = getIntent().getStringExtra("ID_USER");

        dialog = new CustomDialog(SeeProfileActivity.this);
        toast = new CustomToast(SeeProfileActivity.this);
        mDatabase = new DatabaseFirestore();

        findViewId();

        onClickSetting();

        firebaseRecycler(idUser);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SeeProfileActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        dialog.showLoading();
        mDatabase.getOtherUserData(idUser).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    String uid = document.getString("uid");
                    String nickname = document.getString("nickname");
                    String title = document.getString("title");
                    String avatar = document.getString("avatar");
                    String status = document.getString("status");
                    String charData = document.getString("usechar");
                    String wallData = document.getString("usewall");


                    if (avatar != null && !avatar.equals("default")) {
                        Glide.with(SeeProfileActivity.this).load(avatar).into(avatarView);
                    }

                    Glide.with(SeeProfileActivity.this).load(charData).into(charView);
                    Glide.with(SeeProfileActivity.this).load(wallData).into(wallView);
                    titleTxt.setText(title);
                    nameTxt.setText(nickname);
                    statusTxt.setText(status);

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
                    dialog.dismissLoading();
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

    private void findViewId() {
        settingBtn = findViewById(R.id.see_profile_setting);
        homeBtn = findViewById(R.id.back_btn);
        charView = findViewById(R.id.see_profile_char_close_up);
        wallView = findViewById(R.id.see_profile_wallpaper);
        avatarView = findViewById(R.id.see_profile_avatar);
        titleTxt = findViewById(R.id.see_profile_title);
        nameTxt = findViewById(R.id.see_profile_username);
        statusTxt = findViewById(R.id.see_profile_status);
        lastKanji = findViewById(R.id.kanji_last_score);
        bestKanji = findViewById(R.id.kanji_best_score);
        lastPhrases = findViewById(R.id.phrase_last_score);
        bestPhrases = findViewById(R.id.phrase_best_score);
        lastGrammar = findViewById(R.id.grammar_last_score);
        bestGrammar = findViewById(R.id.grammar_best_score);
        recyclerView = findViewById(R.id.list_see_profile_message);
        inputMessageText = findViewById(R.id.see_profile_message);
        sendBtn = findViewById(R.id.see_profile_message_send);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = Paper.book().read("current_user");
                final String message = inputMessageText.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    dialog.showLoading();
                    final String uniqueID = UUID.randomUUID().toString();
                    final Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("from", uid);
                    messageMap.put("to", idUser);
                    messageMap.put("read", false);
                    messageMap.put("sentdate", new Date());
                    messageMap.put("message", message);
                    messageMap.put("id", uniqueID);
                    FirebaseFirestore.getInstance().collection("user").document(idUser)
                            .collection("message").document(uniqueID)
                            .set(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseFirestore.getInstance().collection("user").document(uid)
                                        .collection("message").document(uniqueID).set(messageMap);
                                toast.showToast("Successfully sent.");
                                dialog.dismissLoading();
                                inputMessageText.setText("");
                                recyclerView.scrollToPosition(0);
                            } else {
                                toast.showToast("Cannot send, check your connection.");
                                dialog.dismissLoading();
                            }
                        }
                    });
                }
            }
        });
    }

    private void firebaseRecycler(String uid) {
        Query query = FirebaseFirestore.getInstance()
                .collection("user")
                .document(uid)
                .collection("message")
                .orderBy("sentdate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Message, SeeProfileActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull final SeeProfileActivity.ViewHolder holder, int position, @NonNull Message model) {

                String from = model.getFrom();
                String message = model.getMessage();
                Date sentdate = model.getSentdate();

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
                String date = df.format(sentdate);

                holder.setDate(date);
                holder.setMessage(message);


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
            public SeeProfileActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_message, group, false);

                return new SeeProfileActivity.ViewHolder(view);
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
                Glide.with(SeeProfileActivity.this).load(string).into(imageView);
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
}
