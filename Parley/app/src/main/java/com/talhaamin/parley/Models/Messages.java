package com.talhaamin.parley.Models;

import android.app.Notification;

import java.util.Date;

/**
 * Created by Talha Amin on 1/5/2017.
 */
public class Messages {
    public String from;
    public String timestamp;
    public String message;
    public int type;
    public String displayName;

    public Messages(){}

    public Messages(String from, String timestamp, String message, int type, String displayName) {
        this.from = from;
        this.timestamp = timestamp;
        this.message = message;
        this.type = type;
        this.displayName = displayName;
    }
}
