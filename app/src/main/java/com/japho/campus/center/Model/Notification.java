package com.japho.campus.center.Model;

public class Notification {
    private String userid;
    private String text;
    private String postid;
    private boolean isPost;

    private String type;

    public Notification() {
    }

    public Notification(String userid, String text, String postid, boolean isPost,String type) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.isPost = isPost;
        this.type =type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIsPost() {
        return isPost;
    }

    public void setIsPost(boolean post) {
        isPost = post;
    }

    public String getType()
    {
        return type;
    }
}
