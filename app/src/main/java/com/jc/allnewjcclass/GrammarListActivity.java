package com.jc.allnewjcclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class GrammarListActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private ListView listView;
    private Spinner spinner;
    private Button backBtn;

    // Custom
    private CustomDialog dialog;
    private CustomToast toast;
    private CustomSound sound;

    // Data
    private String wallData;
    private String[] grammarTierList = {"Basic", "Intermediate", "Expert", "Master"};
    private List<String> grammarTitleList = new ArrayList<>();
    private List<String> grammarDescList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_list);

        wallData = getIntent().getStringExtra("WALLPAPER");

        dialog = new CustomDialog(GrammarListActivity.this);
        toast = new CustomToast(GrammarListActivity.this);
        sound = new CustomSound(GrammarListActivity.this);

        findViewId();

        onClickSetting();

        Glide.with(GrammarListActivity.this).load(wallData).into(wallView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(GrammarListActivity.this,
                android.R.layout.simple_spinner_item, grammarTierList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dialog.showLoading();
                grammarTitleList.clear();
                grammarDescList.clear();
                firebaseListView(String.valueOf(spinner.getSelectedItem()).toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void firebaseListView(String field) {
        FirebaseFirestore.getInstance().collection("grammar").whereEqualTo("tier", field)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        grammarTitleList.add(documentSnapshot.getString("title"));
                        grammarDescList.add(documentSnapshot.getString("description"));
                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(GrammarListActivity.this,
                            android.R.layout.simple_list_item_1, grammarTitleList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            sound.playBtnClickSound();
                            Intent intent = new Intent(GrammarListActivity.this, DetailGrammarActivity.class);
                            intent.putExtra("WALLPAPER", wallData);
                            intent.putExtra("TITLE", grammarTitleList.get(position));
                            intent.putExtra("DESC", grammarDescList.get(position));
                            startActivity(intent);
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
    protected void onStart() {
        super.onStart();
    }

    private void findViewId() {
        wallView = findViewById(R.id.grammar_list_wallpaper);
        listView = findViewById(R.id.list_grammar);
        spinner = findViewById(R.id.grammar_list_tier_spinner);
        backBtn = findViewById(R.id.back_grammar);
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
}
