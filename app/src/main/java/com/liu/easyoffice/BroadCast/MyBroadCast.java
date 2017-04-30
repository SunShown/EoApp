package com.liu.easyoffice.BroadCast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liu.easyoffice.Activity.ApproveinfoActivity;

import java.lang.reflect.Type;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hui on 2016/11/4.
 */

public class MyBroadCast extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
//            Log.e("receiver", "onReceive: " +intent.getAction()+"  extra"+intent.getExtras());
//
//            String c=bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Gson gson=new Gson();
//            Type type= new TypeToken< Map<String,Object>>(){}.getType();
//            Map<String,Object> rs=gson.fromJson(c,type);
//            String title=bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String msg=bundle.getString(JPushInterface.EXTRA_ALERT);
//            Log.e("receiver", "onReceive: "+title+" msg"+msg+" bundle"+ rs.get("from"));


    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            Log.e("receiver", "onReceive: " +intent.getAction()+"  extra"+intent.getExtras());

            String c=bundle.getString(JPushInterface.EXTRA_EXTRA);
            Gson gson=new Gson();
            Type type= new TypeToken< Map<String,String>>(){}.getType();
            Map<String,String> rs=gson.fromJson(c,type);
           String appId= rs.get("approveId");
            String isGetter=rs.get("isGetter");
            int back=0;
            if ("getter".equals(isGetter)){
        back=1;
            }else if("sender".equals(isGetter)){back=0;}
            Intent intent1=new Intent(context, ApproveinfoActivity.class);
            intent1.putExtra("approveId",Integer.parseInt(appId));
Log.e("mybroad",appId);

            intent1.putExtra("backflag", back);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
//            String title=bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String msg=bundle.getString(JPushInterface.EXTRA_ALERT);
           // Log.e("receiver", "onReceive: "+title+" msg"+msg+" bundle"+ rs.get("1")+"open");
        } else {

        }
    }
}





