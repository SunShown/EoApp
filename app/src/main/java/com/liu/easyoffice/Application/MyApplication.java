package com.liu.easyoffice.Application;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.liu.easyoffice.Activity.MainActivity;
import com.liu.easyoffice.Service.MyService;
import com.liu.easyoffice.Utils.MySharePreference;
import com.liu.easyoffice.pojo.User;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by hui on 2016/9/13.
 */
public class MyApplication extends Application{
    public static MyApplication instance;
    public Typeface kaiTi;
    public static DisplayMetrics dm;
    @Override
    public void onCreate() {
        super.onCreate();
        instance= (MyApplication) getApplicationContext();
        kaiTi=Typeface.createFromAsset(getAssets(),"fonts/kaiti.ttf");//设置字体
        /**
         * 初始化Xutils
         */
        dm=getResources().getDisplayMetrics();
        System.out.println(dm.widthPixels+"  "+dm.heightPixels);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        /**
         * 初始化IMkit
         */
        RongIM.init(this);
        JPushInterface.init(this);//极光推送初始化
        startMyService();


        /**
         * 初始化JPUSH
         */
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
    public static MyApplication getInstance(){
        return instance;
    }
    public Typeface getTypeface(){
        return kaiTi;
    }
    public static int getPhoneHeight(){
        return dm.heightPixels;
    }
    public static int getPhoneWidth(){
        return dm.widthPixels;
    }

    /**
     * 开启服务在后台进行对手机联系人的读取
     */
    public void startMyService(){
        Intent intent=new Intent(this, MyService.class);
        startService(intent);
    }
}
