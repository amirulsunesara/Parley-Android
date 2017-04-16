package com.talhaamin.parley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;

import com.talhaamin.parley.Models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    List<String> chatTopics = new ArrayList<>();
    User user;
    RecyclerView chatRecyclerView;
    ChatListRVadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Chats");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //checking sharedPreference
        user = ((App) this.getApplication()).getProfile();
        Collection<String> chatTopicsValues =  user.chats.values();
        chatTopics = new ArrayList<String>(chatTopicsValues);
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        setRecyclerAdapters();



    }

    public void onResume(){
        super.onResume();
        // put your code here...
        chatRecyclerView.invalidate();
        Log.v("Resume","Resume");
        user = new User();
        user = ((App) this.getApplication()).getProfile();
        Collection<String> chatTopicsValues =  user.chats.values();
        chatTopics = new ArrayList<String>(chatTopicsValues);
        setRecyclerAdapters();
        adapter.notifyDataSetChanged();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent i = new Intent(ChatListActivity.this,MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickPlus(View v)
    {
        Intent intent = new Intent(ChatListActivity.this,UserListActivity.class);
        startActivity(intent);
    }
    public void setRecyclerAdapters()
    {
        adapter = new ChatListRVadapter(this,chatTopics);
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}

