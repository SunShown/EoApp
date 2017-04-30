package com.liu.easyoffice.Pinyin;

import com.liu.easyoffice.pojo.User;

import java.util.Comparator;

/**
 * Created by hui on 2016/10/24.
 */

public class PinyinComparator implements Comparator<User> {
    @Override
    public int compare(User lhs, User rhs) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (lhs.getSortLetters().equals("@")
                || rhs.getSortLetters().equals("#")) {
            return -1;
        } else if (lhs.getSortLetters().equals("#")
                || rhs.getSortLetters().equals("@")) {
            return 1;
        } else {
            return lhs.getSortLetters().compareTo(rhs.getSortLetters());
        }
    }
}
