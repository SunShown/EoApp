package com.liu.easyoffice;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.liu.easyoffice.Pinyin.PinyinComparator;
import com.liu.easyoffice.pojo.User;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        PinyinComparator pin=new PinyinComparator();
        User user=new User();
        user.setSortLetters("A");
        User user1=new User();
        user.setSortLetters("B");
        System.out.println(pin.compare(user,user1));
    }
}