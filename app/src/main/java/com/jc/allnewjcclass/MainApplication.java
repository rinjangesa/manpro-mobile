package com.jc.allnewjcclass;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.jc.allnewjcclass.customs.CustomSound;
import com.jc.allnewjcclass.helpers.LocaleHelper;

import io.paperdb.Paper;

public class MainApplication extends Application {

    private CustomSound sound;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sound = new CustomSound(MainApplication.this);
        Paper.init(MainApplication.this);
        String mute = Paper.book().read("MUTE");
        if (mute != null && mute.equals("OFF")) {
            startService(new Intent(MainApplication.this, CustomSound.class));
        } else if (mute == null) {
            startService(new Intent(MainApplication.this, CustomSound.class));
        } else {
            stopService(new Intent(MainApplication.this, CustomSound.class));
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent(MainApplication.this, CustomSound.class));
    }


}
