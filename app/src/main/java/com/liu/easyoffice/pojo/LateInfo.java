package com.liu.easyoffice.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/7.
 */

public class LateInfo implements Serializable {


    private static final long serialVersionUID = 7690409999419147418L;
    private String userName;//用户名
    private String imgUrl;  //头像URL
    private int lateTimes;  //工作时长
    private String departmentName;  //部门名

    public LateInfo() {
    }

    public LateInfo(String userName, String imgUrl, int lateTimes, String departmentName) {
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.lateTimes = lateTimes;
        this.departmentName = departmentName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(int lateTimes) {
        this.lateTimes = lateTimes;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "LateInfo{" +
                "userName='" + userName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", lateTimes=" + lateTimes +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
