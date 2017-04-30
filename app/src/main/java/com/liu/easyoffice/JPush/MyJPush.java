package com.liu.easyoffice.JPush;

import android.content.Context;
import android.util.Log;

import com.liu.easyoffice.Utils.ToastCommom;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by hui on 2016/11/3.
 */

public class MyJPush  {
    public static void setAlias(final Context cotext, String id){
        JPushInterface.setAliasAndTags(cotext, id, null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if(i==0){
                    ToastCommom.ToastShow(cotext,null,"tag注册成功");
                    Log.e("alias", "注册成功" );
                }else {
                    Log.e("alias", "注册失败"+i );
                }
            }
        });
    }
}
