package com.liu.easyoffice.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.liu.easyoffice.pojo.User;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hui on 2016/10/24.
 */

public class TelContacts {
    public static List<User> getContactsFromPhone(Context context){
        long firstTime=System.currentTimeMillis();

        List<User> users=new ArrayList<>();
        ContentResolver resolver=context.getContentResolver();
        Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()){
            User user=new User();
            int nameIndex=cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contactName=cursor.getString(nameIndex);
            user.setUserName(contactName);
            String contactId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactId,null,null);
            while (phone.moveToNext()){
                String phoneNumber=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber=phoneNumber.replace("-","");
                phoneNumber=phoneNumber.replace(" ","");
                user.setUserId(phoneNumber);
            }
            if(phone!=null&&!phone.isClosed()){
                phone.close();
            }
            users.add(user);
        }
        if(cursor!=null&&!cursor.isClosed()){
            cursor.close();
        }
        long endTime=System.currentTimeMillis();
//        System.out.println(endTime-firstTime);
        Log.e("Time", "getContactsFromPhone: "+(endTime-firstTime) );
        return users;
    }
    public static String getPinYinHeadChar(String str){
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += String.valueOf(pinyinArray[0].charAt(0)).toUpperCase();
            } else {
                convert += String.valueOf(word).toUpperCase();
            }
        }
        return convert;
    }
}
