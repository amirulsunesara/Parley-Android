package com.talhaamin.parley;

import android.app.Application;

import com.talhaamin.parley.Models.User;

import java.util.List;

/**
 * Created by Talha Amin on 1/6/2017.
 */

public class App extends Application {
    public User getProfile() {
        return profile;
    }

    public List<User> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<User> user_list) {
        this.user_list = user_list;
    }



    public void setProfile(User profile) {
        this.profile = profile;
    }

    public User profile = new User();
    public List<User> user_list;



}
