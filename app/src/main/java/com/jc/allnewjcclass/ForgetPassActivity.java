package com.jc.allnewjcclass;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends AppCompatActivity {

    // UI
    private EditText inputEmail;
    private Button sendBtn;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.forget_input_email);
        sendBtn = findViewById(R.id.forget_send_btn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (!TextUtils.isEmpty(email)) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPassActivity.this, "Sent, check your email.", Toast.LENGTH_SHORT).show();
                                        inputEmail.setText("");
                                        finish();
                                    } else {
                                        Toast.makeText(ForgetPassActivity.this, "Failed Sending.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(ForgetPassActivity.this, "Type your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
