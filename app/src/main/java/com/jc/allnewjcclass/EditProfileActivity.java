package com.jc.allnewjcclass;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jc.allnewjcclass.customs.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    // UI
    private ImageView wallView;
    private CircleImageView circleImageView;
    private Button backBtn, editBtn, saveBtn;
    private EditText editNick, editStatus;
    private Spinner editTitle;
    private Button editAvatar;

    // Firebase
    private StorageReference storageReference;

    // Custom
    private CustomDialog dialog;

    // Data
    private String idUser, nickname, title, avatar, status;
    private boolean editState = false;
    private List<String> titles = new ArrayList<>();
    private static final int PICK_IMAGE = 1;
    private Uri avatarUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        idUser = getIntent().getStringExtra("ID_USER");
        String wallData = getIntent().getStringExtra("WALLPAPER");
        nickname = getIntent().getStringExtra("NICKNAME");
        title = getIntent().getStringExtra("TITLE");
        avatar = getIntent().getStringExtra("AVATAR");
        status = getIntent().getStringExtra("STATUS");

        storageReference = FirebaseStorage.getInstance().getReference().child("user_avatar");

        dialog = new CustomDialog(EditProfileActivity.this);

        findViewId();

        onClickSetting();

        Glide.with(EditProfileActivity.this).load(wallData).into(wallView);
        editNick.setText(nickname);
        editStatus.setText(status);
        if (!avatar.equals("default")) {
            Glide.with(EditProfileActivity.this).load(avatar).into(circleImageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog.showLoading();
        FirebaseFirestore.getInstance().collection("user")
                .document(idUser).collection("title")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        titles.add(document.getString("name"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                            android.R.layout.simple_spinner_item, titles);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    editTitle.setAdapter(adapter);
                    int index = titles.indexOf(title);
                    editTitle.setSelection(index);
                    editTitle.setEnabled(false);
                    dialog.dismissLoading();
                } else {
                    Log.d("GETTITLE", "Error getting documents: ", task.getException());
                    dialog.dismissLoading();
                }
            }
        });
    }

    private void findViewId() {
        wallView = findViewById(R.id.edit_profile_wallpaper);
        circleImageView = findViewById(R.id.preview_avatar);
        backBtn = findViewById(R.id.back_btn);
        editBtn = findViewById(R.id.edit_btn);
        saveBtn = findViewById(R.id.save_btn);
        editNick = findViewById(R.id.edit_nickname);
        editTitle = findViewById(R.id.edit_title);
        editAvatar = findViewById(R.id.edit_avatar);
        editStatus = findViewById(R.id.edit_status);

        saveBtn.setVisibility(View.INVISIBLE);
    }

    private void onClickSetting() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editState) {

                    editState = true;
                    editBtn.setText("Cancel");
                    saveBtn.setVisibility(View.VISIBLE);
                    editNick.setEnabled(true);
                    editTitle.setEnabled(true);
                    editAvatar.setEnabled(true);
                    editStatus.setEnabled(true);

                } else {

                    editState = false;
                    editBtn.setText("Edit");
                    saveBtn.setVisibility(View.INVISIBLE);
                    editNick.setEnabled(false);
                    editTitle.setEnabled(false);
                    editAvatar.setEnabled(false);
                    editStatus.setEnabled(false);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = String.valueOf(editNick.getText());
                final String newTitle = String.valueOf(editTitle.getSelectedItem());
                final String newStatus = String.valueOf(editStatus.getText());
                if (!newName.isEmpty()) {
                    if (avatarUri != null) {
                        storageReference = storageReference.child(idUser + ".jpg");
                        storageReference.putFile(avatarUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("nickname", newName);
                                            map.put("avatar", uri.toString());
                                            map.put("title", newTitle);
                                            map.put("status", newStatus);
                                            FirebaseFirestore.getInstance().collection("user")
                                                    .document(idUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Log.d("UPLOAD FOTO", "HARUSNYA" + String.valueOf(task.getException()));
                                }
                            }
                        });
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nickname", newName);
                        map.put("title", newTitle);
                        map.put("status", newStatus);
                        FirebaseFirestore.getInstance().collection("user")
                                .document(idUser).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                dialog.showSuccess();
                            }
                        });
                    }
                } else {
                    dialog.showDanger();
                }

            }
        });
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editTitle.setEnabled(true);

        if (requestCode == PICK_IMAGE) {
            avatarUri = data.getData();
            circleImageView.setImageURI(avatarUri);
        }
    }
}
