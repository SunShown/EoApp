package com.liu.easyoffice.pojo;



public class ApproveColumn {

	public  int id;

	public  int approvetypeid;

	public  String approvecolumncnname;
public String viewId;

	public  String shijicolumn;
  public  String data;
  



public ApproveColumn() {
}

public ApproveColumn(int id, int approvetypeid, String approvecolumncnname,
		String shijicolumn) {
	this.id = id;
	this.approvetypeid = approvetypeid;
	this.approvecolumncnname = approvecolumncnname;
	this.shijicolumn = shijicolumn;
}


@Override
public String toString() {
	return "ApproveColumn [approvecolumncnname=" + approvecolumncnname
			+ ", approvetypeid=" + approvetypeid + ", data=" + data + ", id="
			+ id + ", shijicolumn=" + shijicolumn + "]";
}


   
  

}
