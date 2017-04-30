package com.liu.easyoffice.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;



public class Approveinfo {

	public int approveId;
	
	public String approvetitle;

	public int approvetypeId;
  
	public User sender;

    public Timestamp newTime;

    public int state;

    public int decison;
   public ApproveType type;
    public User newgetter;

    public List<String> imgurl;
    public  List<ApproveState> allstates;
	public Group senderGroup;
	public Approveinfo() {
	}


	public Approveinfo(int approveId, String approvetitle, int approvetypeId, User sender, Timestamp newTime, int state, int decison, ApproveType type, User newgetter, List<String> imgurl, List<ApproveState> allstates, Group senderGroup) {
		this.approveId = approveId;
		this.approvetitle = approvetitle;
		this.approvetypeId = approvetypeId;
		this.sender = sender;
		this.newTime = newTime;
		this.state = state;
		this.decison = decison;
		this.type = type;
		this.newgetter = newgetter;
		this.imgurl = imgurl;
		this.allstates = allstates;
		this.senderGroup = senderGroup;
	}

	@Override
	public String toString() {
		return "Approveinfo [allstates=" + allstates + ", approveId="
				+ approveId + ", approvetitle=" + approvetitle
				+ ", approvetypeId=" + approvetypeId + ", decison=" + decison
				+ ", imgurl=" + imgurl + ", newTime=" + newTime
				+ ", newgetter=" + newgetter + ", sender=" + sender
				+ ", state=" + state + ", type=" + type + "]";
	}

    

}
