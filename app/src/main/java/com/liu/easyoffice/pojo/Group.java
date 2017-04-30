package com.liu.easyoffice.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hui on 2016/9/28.
 */

public class Group implements Serializable{
    private static final long serialVersionUID = -8113049834438882345L;
    private Long tgId;//组id
    private String tgName;//组name
    private Long parentTgId;//父组id
    private String description;//组描述
    private Long tcId;//公司id;
    private List<User> users;
    private Long tgLeaderId;
    private Company company;
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }
    public Long getTgLeaderId() {
        return tgLeaderId;
    }
    public void setTgLeaderId(Long tgLeaderId) {
        this.tgLeaderId = tgLeaderId;
    }
    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Long getTgId() {
        return tgId;
    }
    public void setTgId(Long tgId) {
        this.tgId = tgId;
    }
    public String getTgName() {
        return tgName;
    }
    public void setTgName(String tgName) {
        this.tgName = tgName;
    }
    public Long getParentTgId() {
        return parentTgId;
    }
    public void setParentTgId(Long parentTgId) {
        this.parentTgId = parentTgId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getTcId() {
        return tcId;
    }
    public void setTcId(Long tcId) {
        this.tcId = tcId;
    }

}
