package com.liu.easyoffice.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.liu.easyoffice.pojo.Company;
import com.liu.easyoffice.pojo.Group;
import com.liu.easyoffice.pojo.User;

import java.sql.Timestamp;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hui on 2016/9/22.
 */

public class MySharePreference {
    public static boolean isFirst(Context context){
        SharedPreferences preference=context.getSharedPreferences("isFirst",MODE_PRIVATE);
        return preference.getBoolean("isFirst",true);
    }
    public static User getCurrentUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        String userName=preferences.getString("userName","");
        String imgUrl=preferences.getString("imgUrl","");
        String birthday=preferences.getString("userBirth","1992-08-08 00:00:00.0");
        Log.e("birthday", "getCurrentUser: "+birthday );
        Long id=preferences.getLong("id",-1L);
        String address=preferences.getString("userAddress","");
        int sex=preferences.getInt("userSex",0);
        Log.e("sharePre", "getCurrentUser: "+id);
        User user=new User();
        user.setId(id);
        user.setAddress(address);
        user.setSex(sex);
        user.setBirthday(Timestamp.valueOf(birthday));
        user.setUserId(userId);
        user.setUserName(userName);
        user.setImgUrl(imgUrl);
        return user;
    }
    public static Company getCurrentCompany(Context context){
        SharedPreferences preferences=context.getSharedPreferences("company", MODE_PRIVATE);
        Long tcId=preferences.getLong("tcId",-1);
        String tcName=preferences.getString("tcName",null);
        Company company=new Company();
        company.setTcId(tcId);
        company.setTcName(tcName);
        return company;
    }
    public static Group getCurrentCroup(Context context){
        SharedPreferences preferences=context.getSharedPreferences("group", MODE_PRIVATE);
        Long tgId=preferences.getLong("tgId",-1);
        String tgName=preferences.getString("tgName",null);
        Group group=new Group();
        group.setTgId(tgId);
        group.setTgName(tgName);
        return group;
    }
    public static boolean setCurrentUser(Context context,User user){
        SharedPreferences sp = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(user.getUserName()!=null) {
            editor.putString("userName", user.getUserName());
        }
        if(user.getBirthday()!=null){
            editor.putString("userBirth", user.getBirthday().toString() + "");
        }
        if(user.getAddress()!=null){
            editor.putString("userAddress", user.getAddress());
        }
        if(user.getUserPosition()!=null){
            editor.putString("userPosition", user.getUserPosition());
        }
        if(user.getImgUrl()!=null){
            editor.putString("imgUrl", user.getImgUrl());
        }
        if(user.getUserPwd()!=null){
            editor.putString("userPassword", user.getUserId());
        }
        editor.putInt("userSex", user.getSex());
        boolean isSucess=editor.commit();
        return isSucess;
    }
    /**
     * 是不是第一次登录
     * @param context
     */
    public static void setNotFirst(Context context){
        SharedPreferences sp = context.getSharedPreferences("isFirst", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst",false);
        editor.commit();
    }
    public static boolean setCurrentComapny(Context context, Company company){
        SharedPreferences preferences = context.getSharedPreferences("company", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("tcId",company.getTcId());
        editor.putString("tcName", company.getTcName());
        boolean isSucess=editor.commit();
        return isSucess;
    }
    /**
     * 清除所有share
     */
    public static void clearSharePreference(Context context){
        context.getSharedPreferences("user",MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("group",MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("company",MODE_PRIVATE).edit().clear().commit();
    }
    public static boolean setCurrentGroup(Context context,Group group){
        SharedPreferences preferences = context.getSharedPreferences("group", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("tgId", group.getTgId());
        editor.putString("tgName",group.getTgName());
        editor.putLong("parentGId",group.getParentTgId());
        return editor.commit();
    }
}
