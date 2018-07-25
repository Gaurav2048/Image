package com.image.imagenext.imagenextbig.Models;

import java.io.Serializable;

/**
 * Created by saurav on 7/24/2018.
 */

public class BranchComment implements Serializable {
    String sub_comment_name;
    String sub_comment_user_id;
    String sub_comment;
    String sub_comment_time;
    String sub_comment_image_url;

    public BranchComment(String sub_comment_name, String sub_comment_user_id, String sub_comment, String sub_comment_time, String sub_comment_image_url) {
        this.sub_comment_name = sub_comment_name;
        this.sub_comment_user_id = sub_comment_user_id;
        this.sub_comment = sub_comment;
        this.sub_comment_time = sub_comment_time;
        this.sub_comment_image_url = sub_comment_image_url;
    }

    public String getSub_comment_name() {

        return sub_comment_name;
    }

    public void setSub_comment_name(String sub_comment_name) {
        this.sub_comment_name = sub_comment_name;
    }

    public String getSub_comment_user_id() {
        return sub_comment_user_id;
    }

    public void setSub_comment_user_id(String sub_comment_user_id) {
        this.sub_comment_user_id = sub_comment_user_id;
    }

    public String getSub_comment() {
        return sub_comment;
    }

    public void setSub_comment(String sub_comment) {
        this.sub_comment = sub_comment;
    }

    public String getSub_comment_time() {
        return sub_comment_time;
    }

    public void setSub_comment_time(String sub_comment_time) {
        this.sub_comment_time = sub_comment_time;
    }

    public String getSub_comment_image_url() {
        return sub_comment_image_url;
    }

    public void setSub_comment_image_url(String sub_comment_image_url) {
        this.sub_comment_image_url = sub_comment_image_url;
    }
}
