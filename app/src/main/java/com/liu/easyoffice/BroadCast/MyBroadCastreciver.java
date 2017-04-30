package com.liu.easyoffice.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.liu.easyoffice.Activity.ApproveinfoActivity;

/**
 * Created by zyc on 2016/10/29.
 */

public class MyBroadCastreciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.liu.easyoffice.approveinfoupdate".equals(intent.getAction())){
            Bundle bundle = intent.getBundleExtra("bundle1");
            Intent intent1=new Intent(context,ApproveinfoActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("bundle2",bundle);
            context.startActivity(intent1);
    }}
}
