package com.japho.campus.center.Model;

import java.util.Date;

public class Post {

    private String description;
    private String imageurl;
    private String postid;
    private String publisher;
    private String audience;
    private String visible;
    private String tribe;
    private String county;
    private String type;

    private Date exp;

    public Post() {
    }

    public Post(String description, String imageurl, String postid, String publisher,String audience,String visible,String tribe,String county,String type,Date exp) {
        this.description = description;
        this.imageurl = imageurl;
        this.postid = postid;
        this.publisher = publisher;
        this.audience = audience;
        this.visible = visible;
        this.tribe=tribe;
        this.county=county;
        this.type=type;
        this.exp=exp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getAudience() {
        return audience;
    }
    public String getVisible() {
        return visible;
    }
    public String getTribe()
    {
        return tribe;
    }
    public String getCounty()
    {
        return  county;
    }
    public String getType()
    {
        return type;
    }
    public Date getExp()
    {
        return exp;
    }
}
