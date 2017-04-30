package com.liu.easyoffice.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CompanySignSettingInfo implements Serializable {
    private static final long serialVersionUID = 1235893901871216822L;
    private double mDoubleLatitude;  //纬度
    private double mDoubleLongitude;  //经度
    private String mStringTitle;
    private String mStringSnippet;
    private int mIntEffectiveRange;   //有效距离
    private int mIntElasticTime;
    private int mIntlateAndAbsen;
    private int mIntEarliestTime;
    private int mIntOnRemind;
    private int mIntOffRemind;
    private int mIntHardAveTime;
    private String mOnWeekString;
    private String mOffWeekString;
    private String onTimeString;
    private String offTimeString;
    private boolean autoBreakByLaw;     //是否根据国家法定节假日排班
    private boolean isNextDayBegin;    //是否从明天开始考勤打卡，默认是false，说明是从下周开始的

    public CompanySignSettingInfo() {
    }

    public CompanySignSettingInfo(double doubleLatitude, double doubleLongitude, String stringTitle,
                                  String stringSnippet, int intEffectiveRange, int intElasticTime,
                                  int intlateAndAbsen, int intEarliestTime, int intOnRemind,
                                  int intOffRemind, int intHardAveTime, String onWeekString,
                                  String offWeekString, String onTimeString, String offTimeString,
                                  boolean autoBreakByLaw, boolean isNextDayBegin) {
        mDoubleLatitude = doubleLatitude;
        mDoubleLongitude = doubleLongitude;
        mStringTitle = stringTitle;
        mStringSnippet = stringSnippet;
        mIntEffectiveRange = intEffectiveRange;
        mIntElasticTime = intElasticTime;
        mIntlateAndAbsen = intlateAndAbsen;
        mIntEarliestTime = intEarliestTime;
        mIntOnRemind = intOnRemind;
        mIntOffRemind = intOffRemind;
        mIntHardAveTime = intHardAveTime;
        mOnWeekString = onWeekString;
        mOffWeekString = offWeekString;
        this.onTimeString = onTimeString;
        this.offTimeString = offTimeString;
        this.autoBreakByLaw = autoBreakByLaw;
        this.isNextDayBegin = isNextDayBegin;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double getDoubleLatitude() {
        return mDoubleLatitude;
    }

    public void setDoubleLatitude(double doubleLatitude) {
        mDoubleLatitude = doubleLatitude;
    }

    public double getDoubleLongitude() {
        return mDoubleLongitude;
    }

    public void setDoubleLongitude(double doubleLongitude) {
        mDoubleLongitude = doubleLongitude;
    }

    public String getStringTitle() {
        return mStringTitle;
    }

    public void setStringTitle(String stringTitle) {
        mStringTitle = stringTitle;
    }

    public String getStringSnippet() {
        return mStringSnippet;
    }

    public void setStringSnippet(String stringSnippet) {
        mStringSnippet = stringSnippet;
    }

    public int getIntEffectiveRange() {
        return mIntEffectiveRange;
    }

    public void setIntEffectiveRange(int intEffectiveRange) {
        mIntEffectiveRange = intEffectiveRange;
    }

    public int getIntElasticTime() {
        return mIntElasticTime;
    }

    public void setIntElasticTime(int intElasticTime) {
        mIntElasticTime = intElasticTime;
    }

    public int getIntlateAndAbsen() {
        return mIntlateAndAbsen;
    }

    public void setIntlateAndAbsen(int intlateAndAbsen) {
        mIntlateAndAbsen = intlateAndAbsen;
    }

    public int getIntEarliestTime() {
        return mIntEarliestTime;
    }

    public void setIntEarliestTime(int intEarliestTime) {
        mIntEarliestTime = intEarliestTime;
    }

    public int getIntOnRemind() {
        return mIntOnRemind;
    }

    public void setIntOnRemind(int intOnRemind) {
        mIntOnRemind = intOnRemind;
    }

    public int getIntOffRemind() {
        return mIntOffRemind;
    }

    public void setIntOffRemind(int intOffRemind) {
        mIntOffRemind = intOffRemind;
    }

    public int getIntHardAveTime() {
        return mIntHardAveTime;
    }

    public void setIntHardAveTime(int intHardAveTime) {
        mIntHardAveTime = intHardAveTime;
    }

    public String getOnWeekString() {
        return mOnWeekString;
    }

    public void setOnWeekString(String onWeekString) {
        mOnWeekString = onWeekString;
    }

    public String getOffWeekString() {
        return mOffWeekString;
    }

    public void setOffWeekString(String offWeekString) {
        mOffWeekString = offWeekString;
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

    public boolean isAutoBreakByLaw() {
        return autoBreakByLaw;
    }

    public void setAutoBreakByLaw(boolean autoBreakByLaw) {
        this.autoBreakByLaw = autoBreakByLaw;
    }

    public boolean isNextDayBegin() {
        return isNextDayBegin;
    }

    public void setNextDayBegin(boolean nextDayBegin) {
        isNextDayBegin = nextDayBegin;
    }

    @Override
    public String toString() {
        return "CompanySignSettingInfo{" +
                "mDoubleLatitude=" + mDoubleLatitude +
                ", mDoubleLongitude=" + mDoubleLongitude +
                ", mStringTitle='" + mStringTitle + '\'' +
                ", mStringSnippet='" + mStringSnippet + '\'' +
                ", mIntEffectiveRange=" + mIntEffectiveRange +
                ", mIntElasticTime=" + mIntElasticTime +
                ", mIntlateAndAbsen=" + mIntlateAndAbsen +
                ", mIntEarliestTime=" + mIntEarliestTime +
                ", mIntOnRemind=" + mIntOnRemind +
                ", mIntOffRemind=" + mIntOffRemind +
                ", mIntHardAveTime=" + mIntHardAveTime +
                ", mOnWeekString='" + mOnWeekString + '\'' +
                ", mOffWeekString='" + mOffWeekString + '\'' +
                ", onTimeString='" + onTimeString + '\'' +
                ", offTimeString='" + offTimeString + '\'' +
                ", autoBreakByLaw=" + autoBreakByLaw +
                ", isNextDayBegin=" + isNextDayBegin +
                '}';
    }
}
