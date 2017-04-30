package com.liu.easyoffice.pojo;

import java.util.List;



public class ApproveType {


	public int tyypeId;

	public  String tyypeName;
	public List<ApproveColumn> columns;
	public List<ApproveRule> rules;
	public ApproveType() {
	}
	






	public ApproveType(int tyypeId, String tyypeName,
			List<ApproveColumn> columns) {
		this.tyypeId = tyypeId;
		this.tyypeName = tyypeName;
		this.columns = columns;
	}



	@Override
	public String toString() {
		return "ApproveType [columns=" + columns + ", tyypeId=" + tyypeId
				+ ", tyypeName=" + tyypeName + "]";
	}

	

	
	
	
}
