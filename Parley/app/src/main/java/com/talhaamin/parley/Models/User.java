package com.talhaamin.parley.Models;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Talha Amin on 1/4/2017.
 */

public class User {
    public String userId;
    public String displayName;
    public String password;
    public HashMap<String,String> chats = new HashMap<String,String>();
    public boolean isSelected = false;
    public ImageView picture;
    public String pic_url;


    public User(String pic_url, String userId, String displayName, String password, HashMap<String, String> chats, boolean isSelected, ImageView picture) {
        this.pic_url = pic_url;
        this.userId = userId;
        this.displayName = displayName;
        this.password = password;
        this.chats = chats;
        this.isSelected = isSelected;
        this.picture = picture;
    }


    public User(){}

}
