package com.image.imagenext.imagenextbig.Models;

import java.io.Serializable;

/**
 * Created by saurav on 7/24/2018.
 */

public class usermodel implements Serializable {

    String name;
    String username;
    String profile_pic;
    String follower;
    String following;

    public String getName() {
        return name;
    }

    public usermodel(String name, String username, String profile_pic, String follower, String following) {
        this.name = name;
        this.username = username;
        this.profile_pic = profile_pic;
        this.follower = follower;
        this.following = following;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
