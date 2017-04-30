package com.liu.easyoffice;

import com.liu.easyoffice.Pinyin.PinyinComparator;
import com.liu.easyoffice.pojo.User;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        PinyinComparator pin=new PinyinComparator();
        User user=new User();
        user.setSortLetters("liu");
        User user1=new User();
        user1.setSortLetters("B");
        System.out.println(pin.compare(user,user1));
    }

}