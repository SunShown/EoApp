package com.liu.easyoffice.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liu.easyoffice.Utils.FileStore;
import com.liu.easyoffice.Utils.TelContacts;
import com.liu.easyoffice.pojo.User;

import java.util.List;

/**
 * Created by hui on 2016/11/4.
 */

public class MyService extends Service {
    public static final String TAG="Myservice";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate: " );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " );

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = TelContacts.getContactsFromPhone(getApplicationContext());
                FileStore fileStore=new FileStore(getApplicationContext(),"contacts.txt");
                fileStore.saveList(users);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
