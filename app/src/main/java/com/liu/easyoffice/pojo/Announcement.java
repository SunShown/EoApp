package com.liu.easyoffice.pojo;

import java.io.Serializable;

/**
 * Created by candy on 2016/11/2.
 */

public class Announcement implements Serializable{
    private static final long serialVersionUID = 2918693107009778397L;
    private Long aid;//公告
    private String atitle;//公告标题
    private String author;//作者
    private String authorId;//作者ID
    private String acontent;//公告内容
    private String atime;//公告发布时间
    //图片集合
    private String img_1;
    private String img_2;
    private String img_3;
    private String img_4;
    private String img_5;
    private String img_6;
    private String img_7;
    private String img_8;
    private String img_9;

    public  Announcement(){}
    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAcontent() {
        return acontent;
    }

    public void setAcontent(String acontent) {
        this.acontent = acontent;
    }

    public String getAtime() {
        return atime;
    }

    public void setAtime(String atime) {
        this.atime = atime;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public void setImg_3(String img_3) {
        this.img_3 = img_3;
    }

    public String getImg_4() {
        return img_4;
    }

    public void setImg_4(String img_4) {
        this.img_4 = img_4;
    }

    public String getImg_5() {
        return img_5;
    }

    public void setImg_5(String img_5) {
        this.img_5 = img_5;
    }

    public String getImg_6() {
        return img_6;
    }

    public void setImg_6(String img_6) {
        this.img_6 = img_6;
    }

    public String getImg_7() {
        return img_7;
    }

    public void setImg_7(String img_7) {
        this.img_7 = img_7;
    }

    public String getImg_8() {
        return img_8;
    }

    public void setImg_8(String img_8) {
        this.img_8 = img_8;
    }

    public String getImg_9() {
        return img_9;
    }

    public void setImg_9(String img_9) {
        this.img_9 = img_9;
    }
}
