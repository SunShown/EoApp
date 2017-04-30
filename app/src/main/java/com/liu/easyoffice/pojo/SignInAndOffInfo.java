package com.liu.easyoffice.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/1.
 */

public class SignInAndOffInfo implements Serializable {
    //加班与不加班所要生成的数据：员工ID，公司Id，签到状态，日期，上班时间(还有下班时间，为统计做准备)，还有工作时长
    private long employeeIdLong;       //员工Id
    private long companyIdLong;
    private int signStatusIdInt;  //签到状态，转换成int类型
    private int signOffStatusIdInt;   //签退状态，转换成int类型
    private String remarkString;
    private String dateString;   //日期暂为String类型，服务器端收到数据，服务器端进行处理
    private String onTimeString;   //时间暂为String类型，服务器端收到数据，服务器端进行处理
    private String offTimeString;  //下班时间
    private long workTimesLong;       //上班时长


    private int lateTimes;      //迟到时间数
    private int earlyOffTimes;   //早退时间数

    public SignInAndOffInfo() {
    }

    public SignInAndOffInfo(long employeeIdLong, long companyIdLong, int signStatusIdInt,
                            int signOffStatusIdInt, String remarkString, String dateString,
                            String onTimeString, String offTimeString, long workTimesLong, int lateTimes,
                            int earlyOffTimes) {
        this.employeeIdLong = employeeIdLong;
        this.companyIdLong = companyIdLong;
        this.signStatusIdInt = signStatusIdInt;
        this.signOffStatusIdInt = signOffStatusIdInt;
        this.remarkString = remarkString;
        this.dateString = dateString;
        this.onTimeString = onTimeString;
        this.offTimeString = offTimeString;
        this.workTimesLong = workTimesLong;
        this.lateTimes = lateTimes;
        this.earlyOffTimes = earlyOffTimes;
    }

    public long getEmployeeIdLong() {
        return employeeIdLong;
    }

    public void setEmployeeIdLong(long employeeIdLong) {
        this.employeeIdLong = employeeIdLong;
    }

    public long getCompanyIdLong() {
        return companyIdLong;
    }

    public void setCompanyIdLong(long companyIdLong) {
        this.companyIdLong = companyIdLong;
    }

    public int getSignStatusIdInt() {
        return signStatusIdInt;
    }

    public void setSignStatusIdInt(int signStatusIdInt) {
        this.signStatusIdInt = signStatusIdInt;
    }

    public int getSignOffStatusIdInt() {
        return signOffStatusIdInt;
    }

    public void setSignOffStatusIdInt(int signOffStatusIdInt) {
        this.signOffStatusIdInt = signOffStatusIdInt;
    }

    public String getRemarkString() {
        return remarkString;
    }

    public void setRemarkString(String remarkString) {
        this.remarkString = remarkString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getOnTimeString() {
        return onTimeString;
    }

    public void setOnTimeString(String onTimeString) {
        this.onTimeString = onTimeString;
    }

    public String getOffTimeString() {
        return offTimeString;
    }

    public void setOffTimeString(String offTimeString) {
        this.offTimeString = offTimeString;
    }

    public long getWorkTimesLong() {
        return workTimesLong;
    }

    public void setWorkTimesLong(long workTimesLong) {
        this.workTimesLong = workTimesLong;
    }

    public int getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(int lateTimes) {
        this.lateTimes = lateTimes;
    }

    public int getEarlyOffTimes() {
        return earlyOffTimes;
    }

    public void setEarlyOffTimes(int earlyOffTimes) {
        this.earlyOffTimes = earlyOffTimes;
    }

    @Override
    public String toString() {
        return "SignInAndOffInfo{" +
                "employeeIdLong=" + employeeIdLong +
                ", companyIdLong=" + companyIdLong +
                ", signStatusIdInt=" + signStatusIdInt +
                ", signOffStatusIdInt=" + signOffStatusIdInt +
                ", remarkString='" + remarkString + '\'' +
                ", dateString='" + dateString + '\'' +
                ", onTimeString='" + onTimeString + '\'' +
                ", offTimeString='" + offTimeString + '\'' +
                ", workTimesLong=" + workTimesLong +
                ", lateTimes=" + lateTimes +
                ", earlyOffTimes=" + earlyOffTimes +
                '}';
    }
}
