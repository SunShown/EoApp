package com.liu.easyoffice.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by hui on 2016/9/22.
 */

public class User implements Serializable{
    private static final long serialVersionUID = -2726348499661457293L;
    private String userName;//用户名
    private String userId;//用户手机号
    private String userPwd;//用户密码
    private String userToken;//token
    private String imgUrl;
    private long id;
    private Timestamp birthday;
    private String address;
    private int sex;
    private String userPosition;
    private boolean isChecked=false;
    private boolean isExist=false;//判断此成员在不在当前组内
    private String sortLetters;//名字首字母
    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean isExist) {
        this.isExist = isExist;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", userId=" + userId
                + ", userPwd=" + userPwd + ", userToken=" + userToken
                + ", imgUrl=" + imgUrl + ", id=" + id + ", birthday="
                + birthday + ", address=" + address + ", sex=" + sex
                + ", userPosition=" + userPosition + "]";
    }

    public long getId() {
        return id;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public void setId(long id) {
        this.id = id;
    }

    public	Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public User(String userName, String userId, String userPwd,
                String userToken, String imgUrl, Timestamp birthday,
                String address, int sex) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userToken = userToken;
        this.imgUrl = imgUrl;
        this.birthday = birthday;
        this.address = address;
        this.sex = sex;
    }

    public User(String userName, String userId, String userPwd,
                String userToken, String imgUrl, long id, Timestamp birthday,
                String address, int sex, String userPosition) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userToken = userToken;
        this.imgUrl = imgUrl;
        this.id = id;
        this.birthday = birthday;
        this.address = address;
        this.sex = sex;
        this.userPosition = userPosition;
    }

    public User(String userName, String userId, String userPwd,
                String userToken, String imgUrl, long id, Timestamp birthday,
                String address, int sex) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userToken = userToken;
        this.imgUrl = imgUrl;
        this.id = id;
        this.birthday = birthday;
        this.address = address;
        this.sex = sex;
    }

    public User() {
    }

    public User(String userName, String userId, String userPwd,
                String userToken, String imaUrl) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userToken = userToken;
        this.imgUrl = imaUrl;
    }
    public User(String userName, String userId, String userPwd) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPwd = userPwd;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUserPwd() {
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imaUrl) {
        this.imgUrl = imaUrl;
    }
}
