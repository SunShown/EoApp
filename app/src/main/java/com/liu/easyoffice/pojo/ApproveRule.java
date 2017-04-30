package com.liu.easyoffice.pojo;

import java.util.Arrays;

/**
 * Created by zyc on 2016/11/3.
 */

public class ApproveRule implements Comparable<ApproveRule> {
 
    public int id;
    
    public int approvetypeId;
  
    public String shunxu;

    public double  limit;

    public int limitcolumnid;

    public long companyId;
    public String[] shunxulist;

    public ApproveRule() {
    }

    public ApproveRule(int id, int approvetypeId, String shunxu, double limit, int limitcolumnid, long companyId, String[] shunxulist) {
        this.id = id;
        this.approvetypeId = approvetypeId;
        this.shunxu = shunxu;
        this.limit = limit;
        this.limitcolumnid = limitcolumnid;
        this.companyId = companyId;
        this.shunxulist = shunxulist;
    }
    @Override
    public String toString() {
        return "ApproveRule [id=" + id + ", approvetypeId=" + approvetypeId
                + ", shunxu=" + shunxu + ", limit=" + limit + ", limitcolumnid="
                + limitcolumnid + ", companyId=" + companyId + ", shunxulist="
                + Arrays.toString(shunxulist) + "]";
    }

    @Override
    public int compareTo(ApproveRule o) {
        if(this.limit>o.limit){return -1; }
        else if(this.limit>o.limit){return 1; }
        else{	return 0;}
    }

}
