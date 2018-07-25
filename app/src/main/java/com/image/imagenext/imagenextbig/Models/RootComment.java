package com.image.imagenext.imagenextbig.Models;

import java.io.Serializable;

/**
 * Created by saurav on 7/23/2018.
 */

public class RootComment implements Serializable{
     String root_comment_name ;
     String root_comment_id;
      String root_comment_user_id ;
     String root_comment;
      String root_comment_time;
      String root_comment_image_url;
      String sub_comment_one_name;
      String sub_comment_one_image_url;
      String sub_comment_one_comment;
      String sub_comment_two_name;
      String sub_comment_two_image_url;
    String sub_comment_two_comment;

    public RootComment(String root_comment_name, String root_comment_id, String root_comment_user_id, String root_comment, String root_comment_time, String root_comment_image_url) {
        this.root_comment_name = root_comment_name;
        this.root_comment_id = root_comment_id;
        this.root_comment_user_id = root_comment_user_id;
        this.root_comment = root_comment;
        this.root_comment_time = root_comment_time;
        this.root_comment_image_url = root_comment_image_url;
    }

    public RootComment(String root_comment_name, String root_comment_user_id, String root_comment, String root_comment_time, String root_comment_image_url, String sub_comment_one_name, String sub_comment_one_image_url, String sub_comment_one_comment, String sub_comment_two_name, String sub_comment_two_image_url, String sub_comment_two_comment, String root_comment_id) {
        this.root_comment_name = root_comment_name;
        this.root_comment_user_id = root_comment_user_id;
        this.root_comment = root_comment;
        this.root_comment_time = root_comment_time;
        this.root_comment_image_url = root_comment_image_url;
        this.sub_comment_one_name = sub_comment_one_name;
        this.sub_comment_one_image_url = sub_comment_one_image_url;
        this.sub_comment_one_comment = sub_comment_one_comment;
        this.sub_comment_two_name = sub_comment_two_name;
        this.sub_comment_two_image_url = sub_comment_two_image_url;
        this.sub_comment_two_comment = sub_comment_two_comment;
        this.root_comment_id =root_comment_id;
    }

    public String getRoot_comment_id() {
        return root_comment_id;
    }

    public void setRoot_comment_id(String root_comment_id) {
        this.root_comment_id = root_comment_id;
    }

    public String getRoot_comment_name() {

        return root_comment_name;
    }

    public void setRoot_comment_name(String root_comment_name) {
        this.root_comment_name = root_comment_name;
    }

    public String getRoot_comment_user_id() {
        return root_comment_user_id;
    }

    public void setRoot_comment_user_id(String root_comment_user_id) {
        this.root_comment_user_id = root_comment_user_id;
    }

    public String getRoot_comment() {
        return root_comment;
    }

    public void setRoot_comment(String root_comment) {
        this.root_comment = root_comment;
    }

    public String getRoot_comment_time() {
        return root_comment_time;
    }

    public void setRoot_comment_time(String root_comment_time) {
        this.root_comment_time = root_comment_time;
    }

    public String getRoot_comment_image_url() {
        return root_comment_image_url;
    }

    public void setRoot_comment_image_url(String root_comment_image_url) {
        this.root_comment_image_url = root_comment_image_url;
    }

    public String getSub_comment_one_name() {
        return sub_comment_one_name;
    }

    public void setSub_comment_one_name(String sub_comment_one_name) {
        this.sub_comment_one_name = sub_comment_one_name;
    }

    public String getSub_comment_one_image_url() {
        return sub_comment_one_image_url;
    }

    public void setSub_comment_one_image_url(String sub_comment_one_image_url) {
        this.sub_comment_one_image_url = sub_comment_one_image_url;
    }

    public String getSub_comment_one_comment() {
        return sub_comment_one_comment;
    }

    public void setSub_comment_one_comment(String sub_comment_one_comment) {
        this.sub_comment_one_comment = sub_comment_one_comment;
    }

    public String getSub_comment_two_name() {
        return sub_comment_two_name;
    }

    public void setSub_comment_two_name(String sub_comment_two_name) {
        this.sub_comment_two_name = sub_comment_two_name;
    }

    public String getSub_comment_two_image_url() {
        return sub_comment_two_image_url;
    }

    public void setSub_comment_two_image_url(String sub_comment_two_image_url) {
        this.sub_comment_two_image_url = sub_comment_two_image_url;
    }

    public String getSub_comment_two_comment() {
        return sub_comment_two_comment;
    }

    public void setSub_comment_two_comment(String sub_comment_two_comment) {
        this.sub_comment_two_comment = sub_comment_two_comment;
    }
}
