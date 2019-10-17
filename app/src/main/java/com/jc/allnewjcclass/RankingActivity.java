package com.jc.allnewjcclass;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Character;
import com.jc.allnewjcclass.entities.Score;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class RankingActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private ImageButton backBtn;
    private Button kanjiBtn, phrasesBtn, grammarBtn;
    private TextView
            firstPlaceName, firstPlaceScore,
            secondPlaceName, secondPlaceScore,
            thirdPlaceName, thirdPlaceScore;
    private CircleImageView firstPlaceImg, secondPlaceImg, thirdPlaceImg;
    private RecyclerView recyclerView;

    // Firebase UI
    private FirestoreRecyclerAdapter adapter;
    private DatabaseFirestore mDatabase;

    // Custom
    private CustomToast toast;
    private CustomDialog dialog;
    private CustomSound sound;

    // Data
    private ArrayList<String> topThreeList = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Paper.init(RankingActivity.this);
        String wallData = getIntent().getStringExtra("WALLPAPER");

        toast = new CustomToast(RankingActivity.this);
        dialog = new CustomDialog(RankingActivity.this);
        sound = new CustomSound(RankingActivity.this);
        mDatabase = new DatabaseFirestore();

        findViewId();

        onClickSetting();

        firebaseRecycler("bestkanji");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
        recyclerView.setAdapter(adapter);

        Glide.with(RankingActivity.this).load(wallData).into(wallView);
        dialog.showLoading();
        getTopThree("bestkanji");
    }

    private void findViewId() {
        wallView = findViewById(R.id.ranking_wallpaper);
        backBtn = findViewById(R.id.btn_home);
        kanjiBtn = findViewById(R.id.radio_kanji);
        phrasesBtn = findViewById(R.id.radio_phrases);
        grammarBtn = findViewById(R.id.radio_grammar);

        firstPlaceName = findViewById(R.id.ranking_first_place_name);
        firstPlaceScore = findViewById(R.id.ranking_first_place_score);
        firstPlaceImg = findViewById(R.id.ranking_first_place_img);
        secondPlaceName = findViewById(R.id.ranking_second_place_name);
        secondPlaceScore = findViewById(R.id.ranking_second_place_score);
        secondPlaceImg = findViewById(R.id.ranking_second_place_img);
        thirdPlaceName = findViewById(R.id.ranking_third_place_name);
        thirdPlaceScore = findViewById(R.id.ranking_third_place_score);
        thirdPlaceImg = findViewById(R.id.ranking_third_place_img);

        recyclerView = findViewById(R.id.list_ranking);
    }

    private void onClickSetting() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
    }

    private void firebaseRecycler(final String field) {
        Query query = FirebaseFirestore.getInstance()
                .collection("score")
                .orderBy(field, Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Score> options = new FirestoreRecyclerOptions.Builder<Score>()
                .setQuery(query, Score.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Score, RankingActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull final RankingActivity.ViewHolder holder, final int position, @NonNull final Score model) {
                final String id = model.getId();
                String positions = String.valueOf(position + 1);
                final int kanji = model.getBestkanji();
                final int phrases = model.getBestphrases();
                final int grammar = model.getBestgrammar();

                holder.setPosition(positions);

                switch (field) {
                    case "bestkanji": {
                        final int scores = model.getBestkanji();
                        holder.setScore(String.valueOf(scores));
                        break;
                    }
                    case "bestphrases": {
                        final int scores = model.getBestphrases();
                        holder.setScore(String.valueOf(scores));
                        break;
                    }
                    case "bestgrammar": {
                        final int scores = model.getBestgrammar();
                        holder.setScore(String.valueOf(scores));
                        break;
                    }
                }

                FirebaseFirestore.getInstance()
                        .collection("user").document(id)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            final String level = String.valueOf((int) (long) document.get("level"));
                            final String name = document.getString("nickname");
                            final String avatar = document.getString("avatar");
                            holder.setName("Lv. " + level + " " + name);
                            holder.setImg(avatar);

                            String uid = Paper.book().read("current_user");
                            if (!uid.equals(id)) {
                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sound.playBtnClickSound();
                                        dialog.showSeeProfileDialog(id, name, avatar,
                                                String.valueOf(kanji), String.valueOf(phrases)
                                                , String.valueOf(grammar));
                                    }
                                });
                            }
                        }
                    }
                });

            }

            @NonNull
            @Override
            public RankingActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_rank, group, false);

                return new RankingActivity.ViewHolder(view);
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setPosition(String string) {
            TextView textView = itemView.findViewById(R.id.rank_position);
            textView.setText(string);
        }

        void setImg(String string) {
            ImageView imageView = itemView.findViewById(R.id.rank_img);
            if (string.equals("default")) {
                Glide.with(RankingActivity.this).load(R.drawable.placeholder_char).into(imageView);
            } else {
                Glide.with(RankingActivity.this).load(string).into(imageView);
            }
        }

        void setName(String string) {
            TextView textView = itemView.findViewById(R.id.rank_name);
            textView.setText(string);
        }

        void setScore(String string) {
            TextView textView = itemView.findViewById(R.id.rank_score);
            textView.setText(string);
        }
    }

    public void onShopRadioButtonClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((Button) view).isEnabled();
        map.clear();
        topThreeList.clear();
        switch (view.getId()) {
            case R.id.radio_kanji:
                if (checked)
                    dialog.showLoading();
                phrasesBtn.setEnabled(true);
                grammarBtn.setEnabled(true);
                view.setEnabled(false);
                getTopThree("bestkanji");
                firebaseRecycler("bestkanji");
                recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
            case R.id.radio_phrases:
                if (checked)
                    dialog.showLoading();
                kanjiBtn.setEnabled(true);
                grammarBtn.setEnabled(true);
                view.setEnabled(false);
                getTopThree("bestphrases");
                firebaseRecycler("bestphrases");
                recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
            case R.id.radio_grammar:
                if (checked)
                    dialog.showLoading();
                kanjiBtn.setEnabled(true);
                phrasesBtn.setEnabled(true);
                view.setEnabled(false);
                getTopThree("bestgrammar");
                firebaseRecycler("bestgrammar");
                recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
        }
    }

    private void getTopThree(final String field) {
        FirebaseFirestore.getInstance().collection("score")
                .orderBy(field, Query.Direction.DESCENDING)
                .limit(3)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        topThreeList.add(String.valueOf(document.get(field)));
                    }
                    for (int n = 0; n < 3; n = n + 1) {
                        QuerySnapshot queryDocumentSnapshot = task.getResult();
                        final int finalN = n;
                        FirebaseFirestore.getInstance().collection("user")
                                .document(queryDocumentSnapshot.getDocuments().get(n).getId())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    map.put("NAME" + finalN, String.valueOf(document.getString("nickname")));
                                    map.put("AVATAR" + finalN, String.valueOf(document.getString("avatar")));

                                    if (map.size() == 6) {
                                        String name0 = String.valueOf(map.get("NAME0"));
                                        String name1 = String.valueOf(map.get("NAME1"));
                                        String name2 = String.valueOf(map.get("NAME2"));
                                        String avatar0 = String.valueOf(map.get("AVATAR0"));
                                        String avatar1 = String.valueOf(map.get("AVATAR1"));
                                        String avatar2 = String.valueOf(map.get("AVATAR2"));
                                        firstPlaceName.setText(name0);
                                        secondPlaceName.setText(name1);
                                        thirdPlaceName.setText(name2);
                                        if (!avatar0.equals("default")) {
                                            Glide.with(RankingActivity.this).load(avatar0).into(firstPlaceImg);
                                        }
                                        if (!avatar1.equals("default")) {
                                            Glide.with(RankingActivity.this).load(avatar1).into(secondPlaceImg);
                                        }
                                        if (!avatar2.equals("default")) {
                                            Glide.with(RankingActivity.this).load(avatar2).into(thirdPlaceImg);
                                        }
                                    }
                                    dialog.dismissLoading();
                                }

                            }
                        });
                    }
                    firstPlaceScore.setText(topThreeList.get(0));
                    secondPlaceScore.setText(topThreeList.get(1));
                    thirdPlaceScore.setText(topThreeList.get(2));
                } else {
                    toast.showToast("Check your connection");
                    dialog.dismissLoading();
                    Log.d("", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        kanjiBtn.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
