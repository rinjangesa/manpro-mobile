package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.entities.PhraseCategory;

import io.paperdb.Paper;

public class PhraseActivity extends AppCompatActivity {

    // UI
    private Button endlessPhrases;
    private RecyclerView recyclerView;
    private ImageButton homeBtn;
    private ImageView wallView;

    // Firebase UI
    private FirestoreRecyclerAdapter adapter;

    // Custom
    private CustomDialog dialog;
    private CustomSound sound;

    // Data
    private String wallData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase);
        Paper.init(PhraseActivity.this);

        wallData = getIntent().getStringExtra("WALLPAPER");

        dialog = new CustomDialog(PhraseActivity.this);
        sound = new CustomSound(PhraseActivity.this);

        findViewId();

        onClickSetting();

        firebaseRecycler();

        recyclerView.setLayoutManager(new LinearLayoutManager(PhraseActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Glide.with(PhraseActivity.this).load(wallData).into(wallView);
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
        endlessPhrases = findViewById(R.id.endless_phrase);
        homeBtn = findViewById(R.id.btn_home);
        recyclerView = findViewById(R.id.list_phrase);
        wallView = findViewById(R.id.phrase_wallpaper);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
        endlessPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                Intent intent = new Intent(PhraseActivity.this, EndlessActivity.class);
                intent.putExtra("COLLECTION", "phrase_quiz");
                intent.putExtra("WALLPAPER", wallData);
                startActivity(intent);
            }
        });
    }

    private void firebaseRecycler() {
        Query query = FirebaseFirestore.getInstance()
                .collection("phrase_category")
                .orderBy("english");

        FirestoreRecyclerOptions<PhraseCategory> options = new FirestoreRecyclerOptions.Builder<PhraseCategory>()
                .setQuery(query, PhraseCategory.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PhraseCategory, PhraseActivity.PhraseCategoryHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull PhraseActivity.PhraseCategoryHolder holder, int position, @NonNull PhraseCategory model) {
                final String id = model.getId();
                final String english = model.getEnglish();
                String japan = model.getJapan();
                String romaji = model.getRomaji();
                String indonesia = model.getIndonesia();
                String image = model.getImg();

                String lang = Paper.book().read("language");
                if (lang.equals("en")) {
                    holder.setName(english);
                } else if (lang.equals("in")) {
                    holder.setName(indonesia);
                }
                holder.setJap(japan);
                holder.setRomaji(romaji);
                holder.setImg(image);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playBtnClickSound();
                        Bundle b = new Bundle();
                        b.putString("COLLECTION", "phrase_quiz");
                        b.putString("FIELD", english);
                        Intent intent = new Intent(PhraseActivity.this, QuizActivity.class);
                        intent.putExtra("ID", id);
                        intent.putExtra("WALLPAPER", wallData);
                        intent.putExtra("BUNDLE", b);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public PhraseActivity.PhraseCategoryHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_phrase_category, group, false);

                return new PhraseActivity.PhraseCategoryHolder(view);
            }
        };
    }

    private class PhraseCategoryHolder extends RecyclerView.ViewHolder {

        View mView;

        PhraseCategoryHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setName(String string) {
            TextView textView = itemView.findViewById(R.id.category);
            textView.setText(string);
        }

        void setJap(String string) {
            TextView textView = itemView.findViewById(R.id.japan);
            textView.setText(string);
        }

        void setRomaji(String string) {
            TextView textView = itemView.findViewById(R.id.romaji);
            textView.setText(string);
        }

        void setImg(String string) {
            ImageView imageView = itemView.findViewById(R.id.img);
            Glide.with(PhraseActivity.this).load(string).into(imageView);
        }
    }
}
