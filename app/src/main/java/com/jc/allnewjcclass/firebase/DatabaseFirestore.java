package com.jc.allnewjcclass.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DatabaseFirestore {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mCurrentUser;
    private String mUID;
    private int score;
    private int exp_multiplier;
    private int money_multiplier;

    public DatabaseFirestore() {

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            mUID = mCurrentUser.getUid();
        }
    }

    public String getUID() {
        return mUID;
    }

    public Task<DocumentSnapshot> getCurrentUserData() {
        return mFirestore.collection("user").document(mUID).get();
    }

    public Task<DocumentSnapshot> getOtherUserData(String id) {
        return mFirestore.collection("user").document(id).get();
    }

    public Task<Void> updateUser(Map<String, Object> userMap) {
        return mFirestore.collection("user").document(mUID).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public Task<Void> addHistoryDetailQuiz(Map<String, Object> historyDetailMap) {
        return mFirestore.collection("history").document(mUID).set(historyDetailMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public Task<DocumentReference> addHistoryQuiz(String idHistory, Map<String, Object> historyMap) {
        return mFirestore.collection("history").document(mUID).collection(idHistory).add(historyMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });
    }

    public Task<DocumentSnapshot> getCurrentChatData(String string) {

        return mFirestore.collection("character").document(string).get();
    }

    public int CalculatingMoney(int score) {

        this.score = score;

        getCurrentUserData().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String charImg = document.getString("userchar");
                    money_multiplier = (int) (long) mFirestore.collection("character").whereEqualTo("fullimg", charImg).get().getResult().getDocuments().get(0).get("money");

                }
            }
        });
        return this.score * money_multiplier;
    }

    public Task<DocumentSnapshot> getUserData(String id) {
        return mFirestore.collection("user").document(id).get();
    }

}
