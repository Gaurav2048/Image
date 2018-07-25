package com.image.imagenext.imagenextbig.Models;

/**
 * Created by saurav on 7/24/2018.
 */

public class Like {
    String userid;
    String profileImage;
    String name;

    public Like(String userid, String profileImage, String name) {
        this.userid = userid;
        this.profileImage = profileImage;
        this.name = name;
    }

    public String getUserid() {

        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
