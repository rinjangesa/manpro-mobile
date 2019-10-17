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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Character;
import com.jc.allnewjcclass.firebase.DatabaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShopActivity extends AppCompatActivity {

    // UI
    private ImageButton homeBtn;
    private RecyclerView recyclerView;
    private TextView moneyTxt;
    private ImageView wallView;
    private Button charBtn, wallBtn, decorBtn;

    // Firebase UI
    private FirestoreRecyclerAdapter adapter;
    private DatabaseFirestore mDatabase;

    // Custom
    private CustomToast toast;
    private CustomDialog dialog;
    private CustomSound sound;

    // Variable
    private String idUser;
    private int nMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        String wallData = getIntent().getStringExtra("WALLPAPER");
        idUser = getIntent().getStringExtra("ID_USER");

        toast = new CustomToast(ShopActivity.this);
        dialog = new CustomDialog(ShopActivity.this);
        sound = new CustomSound(ShopActivity.this);

        firebaseInit();

        findViewId();

        onClickSetting();

        firebaseRecycler("character");
        recyclerView.setLayoutManager(new GridLayoutManager(ShopActivity.this, 3));
        recyclerView.setAdapter(adapter);
        Glide.with(ShopActivity.this).load(wallData).into(wallView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        charBtn.setEnabled(false);
        adapter.startListening();
        dialog.showLoading();
        mDatabase.getCurrentUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    nMoney = (int) (long) document.get("money");
                    String money = String.valueOf(nMoney);
                    moneyTxt.setText(money);
                    dialog.dismissLoading();
                } else {
                    dialog.dismissLoading();
                    toast.showToast("Check your connection");
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
        homeBtn = findViewById(R.id.btn_home);
        recyclerView = findViewById(R.id.list_shop);
        moneyTxt = findViewById(R.id.shop_money);
        wallView = findViewById(R.id.shop_wallpaper);
        charBtn = findViewById(R.id.radio_character);
        wallBtn = findViewById(R.id.radio_wallpaper);
        decorBtn = findViewById(R.id.radio_decoration);
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
        Query query = FirebaseFirestore.getInstance()
                .collection(collection)
                .orderBy("price", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Character> options = new FirestoreRecyclerOptions.Builder<Character>()
                .setQuery(query, Character.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Character, ShopActivity.ViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull final ShopActivity.ViewHolder holder, int position, @NonNull Character model) {
                final String id = model.getId();
                final String thumbnail = model.getThumbimg();
                final String full = model.getFullimg();
                final String ori = model.getOriimg();
                final String japanname = model.getJapanname();
                final String name = model.getName();
                final String den = model.getDescriptionEN();
                final String did = model.getDescriptionID();
                final String djp = model.getDescriptionJP();
                final int price = model.getPrice();
                final int money = model.getMoney();
                final int exp = model.getExp();

                holder.setThumb(thumbnail);
                holder.setPrice(String.valueOf(price));

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sound.playBtnClickSound();
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", id);
                        map.put("thumbimg", thumbnail);
                        map.put("fullimg", full);
                        map.put("oriimg", ori);
                        map.put("name", name);
                        map.put("japanname", japanname);
                        map.put("money", money);
                        map.put("exp", exp);
                        map.put("descriptionEN", den);
                        map.put("descriptionID", did);
                        map.put("descriptionJP", djp);
                        dialog.showDialogShopBuy(idUser, id, collection, map, price, nMoney);
                    }
                });
            }

            @NonNull
            @Override
            public ShopActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {

                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.single_shop, group, false);

                return new ShopActivity.ViewHolder(view);
            }
        };
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        void setThumb(String string) {
            ImageView imageView = itemView.findViewById(R.id.shop_thumbnail);
            Glide.with(ShopActivity.this).load(string).into(imageView);
        }

        void setPrice(String string) {
            TextView textView = itemView.findViewById(R.id.shop_price);
            textView.setText(string);
        }
    }

    public void onShopRadioButtonClicked(View view) {
        sound.playBtnClickSound();
        boolean checked = ((Button) view).isEnabled();

        switch (view.getId()) {
            case R.id.radio_character:
                if (checked)
                    wallBtn.setEnabled(true);
                decorBtn.setEnabled(true);
                view.setEnabled(false);
                firebaseRecycler("character");
                recyclerView.setLayoutManager(new GridLayoutManager(ShopActivity.this, 3));
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
                recyclerView.setLayoutManager(new GridLayoutManager(ShopActivity.this, 3));
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
                recyclerView.setLayoutManager(new GridLayoutManager(ShopActivity.this, 3));
                recyclerView.setAdapter(adapter);
                adapter.stopListening();
                adapter.startListening();
                break;
        }
    }
}
