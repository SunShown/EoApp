package com.liu.easyoffice.pojo;

import java.io.Serializable;
import java.util.List;

public class QueryCompanySettingInfoBean implements Serializable{
	private double mDoubleLatitude;  //纬度
    private double mDoubleLongitude;  //经度
    private int mIntEffectiveRange;   //有效距离
    private int mIntElasticTime;
    private int mIntlateAndAbsen;
    private int mIntEarliestTime;
    private String onTimeString;
    private String offTimeString;
    private List<String> workDateLists;
	public QueryCompanySettingInfoBean() {
		super();
	}
	public QueryCompanySettingInfoBean(double mDoubleLatitude,
			double mDoubleLongitude, int mIntEffectiveRange,
			int mIntElasticTime, int mIntlateAndAbsen, int mIntEarliestTime,
			String onTimeString, String offTimeString,
			List<String> workDateLists) {
		super();
		this.mDoubleLatitude = mDoubleLatitude;
		this.mDoubleLongitude = mDoubleLongitude;
		this.mIntEffectiveRange = mIntEffectiveRange;
		this.mIntElasticTime = mIntElasticTime;
		this.mIntlateAndAbsen = mIntlateAndAbsen;
		this.mIntEarliestTime = mIntEarliestTime;
		this.onTimeString = onTimeString;
		this.offTimeString = offTimeString;
		this.workDateLists = workDateLists;
	}
	public double getmDoubleLatitude() {
		return mDoubleLatitude;
	}
	public void setmDoubleLatitude(double mDoubleLatitude) {
		this.mDoubleLatitude = mDoubleLatitude;
	}
	public double getmDoubleLongitude() {
		return mDoubleLongitude;
	}
	public void setmDoubleLongitude(double mDoubleLongitude) {
		this.mDoubleLongitude = mDoubleLongitude;
	}
	public int getmIntEffectiveRange() {
		return mIntEffectiveRange;
	}
	public void setmIntEffectiveRange(int mIntEffectiveRange) {
		this.mIntEffectiveRange = mIntEffectiveRange;
	}
	public int getmIntElasticTime() {
		return mIntElasticTime;
	}
	public void setmIntElasticTime(int mIntElasticTime) {
		this.mIntElasticTime = mIntElasticTime;
	}
	public int getmIntlateAndAbsen() {
		return mIntlateAndAbsen;
	}
	public void setmIntlateAndAbsen(int mIntlateAndAbsen) {
		this.mIntlateAndAbsen = mIntlateAndAbsen;
	}
	public int getmIntEarliestTime() {
		return mIntEarliestTime;
	}
	public void setmIntEarliestTime(int mIntEarliestTime) {
		this.mIntEarliestTime = mIntEarliestTime;
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
	public List<String> getWorkDateLists() {
		return workDateLists;
	}
	public void setWorkDateLists(List<String> workDateLists) {
		this.workDateLists = workDateLists;
	}
	@Override
	public String toString() {
		return "QueryCompanySettingInfo [mDoubleLatitude=" + mDoubleLatitude
				+ ", mDoubleLongitude=" + mDoubleLongitude
				+ ", mIntEffectiveRange=" + mIntEffectiveRange
				+ ", mIntElasticTime=" + mIntElasticTime
				+ ", mIntlateAndAbsen=" + mIntlateAndAbsen
				+ ", mIntEarliestTime=" + mIntEarliestTime + ", onTimeString="
				+ onTimeString + ", offTimeString=" + offTimeString
				+ ", workDateLists=" + workDateLists + "]";
	}
	
	
    
    
}
