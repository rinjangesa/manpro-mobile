package com.jc.allnewjcclass;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jc.allnewjcclass.adapters.PhraseExpandableAdapter;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;
import com.jc.allnewjcclass.entities.Phrase;
import com.jc.allnewjcclass.entities.PhraseHeader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PhraseListActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private ExpandableListView expandableListView;
    private Spinner spinner;
    private Button backBtn;

    // Custom
    private CustomDialog dialog;
    private CustomToast toast;
    private CustomSound sound;

    // Data
    private String wallData;
    private List<String> phrasesCategoryList = new ArrayList<>();

    private LinkedHashMap<String, PhraseHeader> phraseSection = new LinkedHashMap<>();
    private ArrayList<PhraseHeader> phraseHeaderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_list);

        wallData = getIntent().getStringExtra("WALLPAPER");

        dialog = new CustomDialog(PhraseListActivity.this);
        toast = new CustomToast(PhraseListActivity.this);
        sound = new CustomSound(PhraseListActivity.this);

        findViewId();

        onClickSetting();

        Glide.with(PhraseListActivity.this).load(wallData).into(wallView);
        dialog.showLoading();
        firebaseGetPhraseCategory();
    }

    private void firebaseGetPhraseCategory() {
        FirebaseFirestore.getInstance().collection("phrase_category").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                phrasesCategoryList.add(documentSnapshot.getString("english"));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(PhraseListActivity.this,
                                    android.R.layout.simple_spinner_item, phrasesCategoryList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    phraseHeaderList.clear();
                                    firebaseGetPhrase(String.valueOf(spinner.getSelectedItem()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
//                            firebaseGetPhrase(String.valueOf(spinner.getSelectedItem()));
                            dialog.dismissLoading();
                        } else {
                            toast.showToast("Check your connection");
                            dialog.dismissLoading();
                        }
                    }
                });
    }

    private void firebaseGetPhrase(String category) {
        dialog.showLoading();
        FirebaseFirestore.getInstance().collection("phrase")
                .whereEqualTo("category", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        addPhrase(documentSnapshot.getString("english"), documentSnapshot.getString("japan"), documentSnapshot.getString("romaji"),
                                documentSnapshot.getString("description"));
                    }
                    PhraseExpandableAdapter expandableAdapter = new PhraseExpandableAdapter(PhraseListActivity.this, phraseHeaderList);
                    expandableListView.setAdapter(expandableAdapter);
                    dialog.dismissLoading();
                }
            }
        });
    }

    private int addPhrase(String category, String japan, String romaji, String description) {

        int groupPosition = 0;

        //check the hash map if the group already exists
        PhraseHeader phraseHeader = phraseSection.get(category);
        //add the group if doesn't exists

        phraseHeader = new PhraseHeader();
        phraseHeader.setName(category);
        phraseSection.put(category, phraseHeader);
        phraseHeaderList.add(phraseHeader);


        //get the children for the group
        ArrayList<Phrase> phrasesList = phraseHeader.getPhraseList();
        //size of the children list
        int listSize = phrasesList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        Phrase phrase = new Phrase();
        phrase.setJapan(japan);
        phrase.setRomaji(romaji);
        phrase.setDescription(description);
        phrasesList.add(phrase);
        phraseHeader.setPhraseList(phrasesList);

        //find the group position inside the list
        groupPosition = phraseHeaderList.indexOf(phraseHeader);
        return groupPosition;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void findViewId() {
        backBtn = findViewById(R.id.back_phrases);
        wallView = findViewById(R.id.phrase_list_wallpaper);
        spinner = findViewById(R.id.phrase_list_category_spinner);
        expandableListView = findViewById(R.id.expand_list_phrases);
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
