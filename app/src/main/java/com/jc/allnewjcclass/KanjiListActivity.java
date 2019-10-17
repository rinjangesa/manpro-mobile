package com.jc.allnewjcclass;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Kanji;

import org.w3c.dom.Text;

import java.util.List;

import io.paperdb.Paper;

public class KanjiListActivity extends AppCompatActivity {

    // UI
    private Button backBtn;
    private RecyclerView recyclerView;
    private TextView kanjiCountTxt;
    private ImageView wallView;

    // Firebase UI
    private FirestoreRecyclerAdapter adapter;

    // Custom
    private CustomToast toast;
    private CustomDialog dialog;
    private CustomSound sound;

    // Variable
    private String nLevel;

    // Data
    private String wallData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_list);

        wallData = getIntent().getStringExtra("WALLPAPER");
        nLevel = getIntent().getExtras().getString("LEVEL");

        toast = new CustomToast(KanjiListActivity.this);
        dialog = new CustomDialog(KanjiListActivity.this);
        sound = new CustomSound(KanjiListActivity.this);

        findViewId();

        onClickSetting();

        firebaseRecycler(nLevel);

        recyclerView.setLayoutManager(new GridLayoutManager(KanjiListActivity.this, 3));
        recyclerView.setAdapter(adapter);

        Glide.with(KanjiListActivity.this).load(wallData).into(wallView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void findViewId() {
        backBtn = findViewById(R.id.back_kanji);
        recyclerView = findViewById(R.id.list_kanji);
        kanjiCountTxt = findViewById(R.id.kanji_count);
        wallView = findViewById(R.id.kanjilist_wallpaper);
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

    private void firebaseRecycler(String level) {
        Query query = FirebaseFirestore.getInstance()
                .collection("kanji")
                .orderBy("stroke")
                .whereEqualTo("grade", level);

        FirestoreRecyclerOptions<Kanji> options = new FirestoreRecyclerOptions.Builder<Kanji>()
                .setQuery(query, Kanji.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Kanji, KanjiListActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull KanjiListActivity.ViewHolder holder, int position, @NonNull Kanji model) {
                final String id = model.getId();
                final String kanji = model.getKanji();
                final String grade = model.getGrade();
                final String stroke = String.valueOf(model.getStroke());
                final String english = model.getEnglish();
                final String indonesia = model.getIndonesia();
                final String example = model.getExample();
                final String zJson = model.getzJson();
                final String onyomi = model.getOnyomi();
                final String kunyomi = model.getKunyomi();

                holder.setKanji(kanji);
                holder.setEng(english);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playBtnClickSound();
                        Bundle b = new Bundle();
                        b.putString("COLLECTION", "kanji_quiz");
                        b.putString("FIELD", kanji);
                        b.putString("GRADE", grade);
                        b.putString("ONYOMI", onyomi);
                        b.putString("KUNYOMI", kunyomi);
                        b.putString("STROKE", stroke);
                        b.putString("ANIMATION", zJson);
                        if (Paper.book().read("language").equals("en")) {
                            b.putString("MEANING", english);
                        } else {
                            b.putString("MEANING", indonesia);
                        }
                        b.putString("EXAMPLE", example);
                        dialog.showDialogKanji(id, wallData, b);
                    }
                });

                kanjiCountTxt.setText("Total : " + getItemCount());

            }

            @NonNull
            @Override
            public KanjiListActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_kanji, group, false);

                return new KanjiListActivity.ViewHolder(view);
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setKanji(String string) {
            TextView textView = itemView.findViewById(R.id.kanji_char);
            textView.setText(string);
        }

        void setEng(String string) {
            TextView textView = itemView.findViewById(R.id.kanji_eng);
            textView.setText(string);
        }
    }
}
