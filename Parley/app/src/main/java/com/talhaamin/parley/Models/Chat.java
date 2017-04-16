package com.talhaamin.parley.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Talha Amin on 1/5/2017.
 */

public class Chat {
    public String topic;
    public HashMap<String,String> members = new HashMap<String,String>();
    public HashMap<String,Object> messages = new HashMap<String,Object>();

    public Chat(){}

    public Chat(String topic,HashMap<String,String> members, HashMap<String,Object> messages) {
        this.topic = topic;
        this.members = members;
        this.messages = messages;
    }
}
