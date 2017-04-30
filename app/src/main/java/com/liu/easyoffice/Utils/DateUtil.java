package com.liu.easyoffice.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String dateToString(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date stringToDate(String dateStr){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
	public static String stringToDate(Date date,Date now) {


		long day = (now.getTime() - date.getTime()) / (24 * 60 * 60 * 1000);

		if (day <= 1) {
			Log.e("Date1", dateToString(date, "HH:mm"));
			return dateToString(date, "HH:mm");

		} else if (day > 1 && day <= 2) {
			Log.e("Date2", "===========");
			return "昨天";


		} else if (day > 2 && day < 7) {
			Log.e("Date2", "天前");
			return day + "天前";


		} else if (day >= 7 && day < 30) {
			return day / 7 + "周前";
		}
		else{
			return dateToString(date);
		}
	}
}
