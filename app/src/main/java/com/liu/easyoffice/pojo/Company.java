package com.liu.easyoffice.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hui on 2016/9/28.
 */

public class Company implements Serializable{

    private static final long serialVersionUID = -1246359322541208701L;
    public Long tcId;
    public String tcName;
    public Timestamp createTime;
    public String tctype;
    public String tcarea;

    public Company(){}

    public String getTctype() {
        return tctype;
    }

    public void setTctype(String tctype) {
        this.tctype = tctype;
    }

    public String getTcarea() {
        return tcarea;
    }

    public void setTcarea(String tcarea) {
        this.tcarea = tcarea;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getTcId() {
        return tcId;
    }

    public void setTcId(Long tcId) {
        this.tcId = tcId;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }
}
