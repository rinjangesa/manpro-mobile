package com.jc.allnewjcclass.customs;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

    private Context c;

    public CustomToast() {
    }

    public CustomToast(Context c) {
        this.c = c;
    }

    public void showToast(String message) {
        Toast.makeText(this.c, message, Toast.LENGTH_SHORT).show();
    }
}
