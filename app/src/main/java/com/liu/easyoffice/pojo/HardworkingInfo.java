package com.liu.easyoffice.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/7.
 */

public class HardworkingInfo implements Serializable {

    private static final long serialVersionUID = -743643951554987909L;
    private String userName;//用户名
    private String imgUrl;  //头像URL
    private int workTimes;  //工作时长
    private String departmentName;  //部门名

    public HardworkingInfo(String userName, String imgUrl, int workTimes, String departmentName) {
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.workTimes = workTimes;
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

    public int getWorkTimes() {
        return workTimes;
    }

    public void setWorkTimes(int workTimes) {
        this.workTimes = workTimes;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "HardworkingInfo{" +
                "userName='" + userName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", workTimes=" + workTimes +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
