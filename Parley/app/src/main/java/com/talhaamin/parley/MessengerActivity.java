package com.talhaamin.parley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talhaamin.parley.Models.Chat;
import com.talhaamin.parley.Models.Messages;
import com.talhaamin.parley.Models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessengerActivity extends AppCompatActivity {

    App app;
    Button btnSend;
    EditText edtMessage;
    String chat_name;
    User mprofile;
    List<Messages> messageslist = new ArrayList<Messages>();
    ProgressDialog progressDialog;


    RecyclerView rv;
    MessengerRVA adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);


        rv = (RecyclerView) this.findViewById(R.id.messageRV);

        btnSend = (Button) this.findViewById(R.id.btnSend);
        edtMessage =(EditText) findViewById(R.id.edtMessage);



        app = ((App) this.getApplication());
        mprofile = app.getProfile();
        Intent i = getIntent();
        chat_name = i.getBundleExtra("bundle").getString(getString(R.string.topic));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(chat_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        setRecyclerAdapters();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(this.getString(R.string.chats)).child(chat_name).child("members");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final List<Object> members = (List<Object>) dataSnapshot.getValue();
                    final List<User> userList = new ArrayList<User>();
                    for (int x = 0; x < members.size(); x++) {
                        final int y = x;
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference(getString(R.string.users)).child((String) members.get(x));
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    User mem = dataSnapshot.getValue(User.class);
                                    userList.add(mem);
                                }

                                if(members.size() -1 == y)
                                {
                                    app.setUser_list(userList);
                                    app.getUser_list().size();
                                    Log.v("membs", String.valueOf(app.getUser_list().size()));

                                    StartGettingMessages();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    public void StartGettingMessages ()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref2 = database.getReference(MessengerActivity.this.getString(R.string.chats)).child(chat_name).child("messages");
        ref2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = dataSnapshot.getValue(Messages.class);
//
//                if(messages.displayName == mprofile.displayName)
//                {
//                    messages.type = 1; // this is me
//                }
//                else
//                {
//                    messages.type = 2;
//                }


                messageslist.add(messages);
                //recyclerview
                rv.scrollToPosition(messageslist.size() -1 );
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onClickSend(View view)
    {
        if(!edtMessage.getText().toString().isEmpty())
        {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(this.getString(R.string.chats)).child(chat_name).child(getString(R.string.messages)).push();
            Messages msgs = new Messages();
            msgs.from = app.getProfile().userId;
            msgs.displayName = app.getProfile().displayName;
            msgs.message = edtMessage.getText().toString();
            DateFormat df = new SimpleDateFormat("MMM,d hh:mm a");
            msgs.timestamp = df.format(new Date()).toString();
            myRef.setValue(msgs);
            edtMessage.setText("");
            //also update recycler view here
        }
    }

    public void setRecyclerAdapters()
    {
        adapter = new MessengerRVA(this,messageslist,mprofile.displayName);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing Data ...");
        progressDialog.show();

        // To Dismiss progress dialog
        //progressDialog.dismiss();
    }
}

