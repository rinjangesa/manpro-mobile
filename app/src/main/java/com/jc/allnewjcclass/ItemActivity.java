package com.jc.allnewjcclass;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.jc.allnewjcclass.entities.Character;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import io.paperdb.Paper;

public class ItemActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private ImageButton homeBtn;
    private Button charBtn, wallBtn, decorBtn;
    private RecyclerView recyclerView;

    // Firebase
    private FirestoreRecyclerAdapter adapter;
    private DatabaseFirestore mDatabase;

    // Custom
    private CustomDialog dialog;
    private CustomSound sound;

    // Data
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        String wallData = getIntent().getStringExtra("WALLPAPER");
        idUser = getIntent().getStringExtra("ID_USER");

        dialog = new CustomDialog(ItemActivity.this);
        sound = new CustomSound(ItemActivity.this);

        findViewId();

        onClickSetting();

        firebaseRecycler("character");
        recyclerView.setLayoutManager(new GridLayoutManager(ItemActivity.this, 3));
        recyclerView.setAdapter(adapter);
        Glide.with(ItemActivity.this).load(wallData).into(wallView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        charBtn.setEnabled(false);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void findViewId() {
        wallView = findViewById(R.id.item_wallpaper);
        homeBtn = findViewById(R.id.btn_home);
        charBtn = findViewById(R.id.radio_character);
        wallBtn = findViewById(R.id.radio_wallpaper);
        decorBtn = findViewById(R.id.radio_decoration);
        recyclerView = findViewById(R.id.list_item);
    }

    private void onClickSetting() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playBtnClickSound();
                finish();
            }
        });
    }

    private void firebaseRecycler(final String collection) {
        if (idUser == null) {
            idUser = Paper.book().read("current_user");
        }
        Query query = FirebaseFirestore.getInstance()
                .collection("user")
                .document(idUser).collection(collection)
                .orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Character> options = new FirestoreRecyclerOptions.Builder<Character>()
                .setQuery(query, Character.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Character, ItemActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull ItemActivity.ViewHolder holder, int position, @NonNull Character model) {
                final String id = model.getId();
                final String japanname = model.getJapanname();
                final String name = model.getName();
                final int exp = model.getExp();
                final int money = model.getMoney();
                final String eng = model.getDescriptionEN();
                final String ind = model.getDescriptionID();
                final String jap = model.getDescriptionJP();
                final String img = model.getFullimg();
                final String thumbnail = model.getThumbimg();

                holder.setThumb(thumbnail);
                holder.setName(name);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playBtnClickSound();
                        dialog.showItem(collection, idUser, id, japanname, name, String.valueOf(exp),
                                String.valueOf(money), eng, ind, jap, img);
                    }
                });
            }

            @NonNull
            @Override
            public ItemActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_shop, group, false);

                return new ItemActivity.ViewHolder(view);
            }
        };
    }

    public void onRadioButtonClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((Button) view).isEnabled();

        switch (view.getId()) {
            case R.id.radio_character:
                if (checked)
                    wallBtn.setEnabled(true);
                decorBtn.setEnabled(true);
                view.setEnabled(false);
                firebaseRecycler("character");
                recyclerView.setLayoutManager(new GridLayoutManager(ItemActivity.this, 3));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
            case R.id.radio_wallpaper:
                if (checked)
                    charBtn.setEnabled(true);
                decorBtn.setEnabled(true);
                view.setEnabled(false);
                firebaseRecycler("wallpaper");
                recyclerView.setLayoutManager(new GridLayoutManager(ItemActivity.this, 3));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
            case R.id.radio_decoration:
                if (checked)
                    charBtn.setEnabled(true);
                wallBtn.setEnabled(true);
                view.setEnabled(false);
                firebaseRecycler("decoration");
                recyclerView.setLayoutManager(new GridLayoutManager(ItemActivity.this, 3));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setThumb(String string) {
            ImageView imageView = itemView.findViewById(R.id.shop_thumbnail);
            Glide.with(ItemActivity.this).load(string).into(imageView);
        }

        void setName(String string) {
            TextView textView = itemView.findViewById(R.id.shop_price);
            textView.setText(string);
        }
    }
}
