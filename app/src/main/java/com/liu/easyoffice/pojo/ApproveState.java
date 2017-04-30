package com.liu.easyoffice.pojo;

import java.sql.Timestamp;
import java.util.List;


public class ApproveState {



	public  int approvestateId	;




	public  User getter;

	public Timestamp decisiontime;
	public  String comment;

	public  int parentstateId;

	public  int decision;

	public  boolean isnew;
public int approveId;
	public boolean isself;
	public  List<String> imgurl;

public ApproveState() {
}

	public ApproveState(List<String> imgurl, int approveId, String comment, Timestamp decisiontime, User getter) {
		this.imgurl = imgurl;
		this.approveId = approveId;
		this.comment = comment;
		this.decisiontime = decisiontime;
		this.getter = getter;
	}

	public ApproveState(int approveId, User getter, Timestamp decisiontime) {
		this.approveId = approveId;
		this.getter = getter;
		this.decisiontime = decisiontime;
	}

	public ApproveState(int approvestateId) {
		this.approvestateId = approvestateId;
	}

	@Override
	public String toString() {
		return "ApproveState{" +
				"approvestateId=" + approvestateId +
				", getter=" + getter +
				", decisiontime=" + decisiontime +
				", comment='" + comment + '\'' +
				", parentstateId=" + parentstateId +
				", decision=" + decision +
				", isnew=" + isnew +
				", isself=" + isself +
				", imgurl=" + imgurl +
				'}';
	}

	public ApproveState(int approvestateId, User getter, Timestamp decisiontime, String comment, int parentstateId, int decision, boolean isnew, boolean isself, List<String> imgurl) {
		this.approvestateId = approvestateId;
		this.getter = getter;
		this.decisiontime = decisiontime;
		this.comment = comment;
		this.parentstateId = parentstateId;
		this.decision = decision;
		this.isnew = isnew;
		this.isself = isself;
		this.imgurl = imgurl;
	}


//public static void main(String[] args) {
//	System.out.println(User.class.getName());
//}
}
