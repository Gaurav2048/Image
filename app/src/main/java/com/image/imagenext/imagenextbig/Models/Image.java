package com.image.imagenext.imagenextbig.Models;

/**
 * Created by saurav on 7/22/2018.
 */

public class Image {
    String caption;
    String latitude;
    String longitude;
    String location;
    String postImage;
    String time;
    String userId;
    String like;
    String postKey;
    String commentCount;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public Image(String caption, String latitude, String longitude, String location, String postImage, String time, String userId, String psotKey, String like, String commentCount) {
        this.caption = caption;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.postImage = postImage;
        this.time = time;
        this.userId = userId;
        this.postKey=psotKey;
        this.like =like;
        this.commentCount = commentCount;


    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }
}
