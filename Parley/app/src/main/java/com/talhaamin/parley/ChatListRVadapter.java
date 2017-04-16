package com.talhaamin.parley;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talhaamin.parley.Models.Chat;
import com.talhaamin.parley.Models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Talha Amin on 1/7/2017.
 */

public class ChatListRVadapter extends RecyclerView.Adapter<ChatListRVadapter.MyViewHolder> {

    int col = 0;
    String[] colors = {"a","b","c","d","e"};

    App app;

    private LayoutInflater inflater;
    List<String> chatdata = Collections.emptyList();


    public ChatListRVadapter(Context context, List<String> chatdata) {
        inflater = LayoutInflater.from(context);
        this.chatdata = chatdata;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_list_item, parent, false);
        MyViewHolder viewholder = new MyViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String chat = chatdata.get(position);
        holder.txtTitle.setText(chat);
        //txtCircle color decider

        holder.txtCircle.setText(chat.substring(0,1).toUpperCase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Intent i = new Intent(view.getContext(),MessengerActivity.class);
                Bundle b = new Bundle();
                b.putString(view.getContext().getString(R.string.topic) ,holder.txtTitle.getText().toString());
                i.putExtra("bundle",b);
                view.getContext().startActivity(i);

//                List<User> user_list = new ArrayList<User>();
//        //        user_list.add(app.getProfile());
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference myRef = database.getReference(view.getContext().getString(R.string.chats)).child(chat).child("members");
//                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                         List<Object> members = (List<Object>) dataSnapshot.getValue();
//                           for (int x = 0; x < members.size(); x++ )
//                           {
//                               final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                               final DatabaseReference myRef = database.getReference(view.getContext().getString(R.string.users)).child((String) members.get(x));
//                               myRef.addValueEventListener(new ValueEventListener() {
//                                   @Override
//                                   public void onDataChange(DataSnapshot dataSnapshot) {
//                                       if(dataSnapshot.exists())
//                                       {
//                                           User mem = dataSnapshot.getValue(User.class);
//                                       }
//                                   }
//
//                                   @Override
//                                   public void onCancelled(DatabaseError databaseError) {
//
//                                   }
//                               });
//                           }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return chatdata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtCircle;

        public MyViewHolder(View itemView) {

            super(itemView);

            txtCircle = (TextView) itemView.findViewById(R.id.txtCircle);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);


        }

    }
//
//    public void setFilter(ArrayList<Result> newList)
//    {
//        data=new ArrayList<>();
//        data.addAll(newList);
//        notifyDataSetChanged();
//    }
//
}
