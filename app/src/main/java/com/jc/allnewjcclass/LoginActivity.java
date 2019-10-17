package com.jc.allnewjcclass;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jc.allnewjcclass.customs.CustomDialog;
import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.customs.CustomToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // UI
    private EditText inputEmail, inputPass;
    private Button loginBtn, forgetBtn;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    // Custom
    private CustomToast toast;
    private CustomDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        toast = new CustomToast(LoginActivity.this);
        loading = new CustomDialog(LoginActivity.this);

        firebaseInit();

        findViewId();

        onClickSetting();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void findViewId() {
        inputEmail = findViewById(R.id.login_input_email);
        inputPass = findViewById(R.id.login_input_pass);
        loginBtn = findViewById(R.id.login_signin_btn);
        forgetBtn = findViewById(R.id.login_forget_btn);
    }

    private void onClickSetting() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.showLoading();

                String email = inputEmail.getText().toString();
                String pass = inputPass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final String uid = task.getResult().getUser().getUid();

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                        FirebaseFirestore.getInstance().collection("user").document(uid)
                                                .update("token_id", instanceIdResult.getToken());
                                    }
                                });

                                DocumentReference docRef = mFire.collection("user").document(uid);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (!document.exists()) {
                                                Map<String, Object> score = new HashMap<>();
                                                score.put("id", uid);
                                                score.put("lastkanji", 0);
                                                score.put("bestkanji", 0);
                                                score.put("lastphrases", 0);
                                                score.put("bestphrases", 0);
                                                score.put("lastgrammar", 0);
                                                score.put("bestgrammar", 0);
                                                mFire.collection("score").document(uid).set(score);
                                            }
                                        } else {
                                            Log.d("ERROR SCORE", "get failed with ", task.getException());
                                        }
                                    }
                                });
                                finish();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                loading.dismissLoading();
                            } else if (!task.isSuccessful()) {
                                loading.dismissLoading();
                                toast.showToast("Invalid username or password");
                            }
                        }
                    });
                } else {
                    loading.dismissLoading();
                    toast.showToast("Please fill the blank");
                }


            }
        });

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });
    }
}
