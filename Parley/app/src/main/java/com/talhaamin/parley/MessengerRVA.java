package com.talhaamin.parley;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talhaamin.parley.Models.Messages;

import java.util.Collections;
import java.util.List;

/**
 * Created by Talha Amin on 1/7/2017.
 */


public class MessengerRVA extends RecyclerView.Adapter<MessengerRVA.MyViewHolder> {

    private LayoutInflater inflater;
    List<Messages> messagedata = Collections.emptyList();
    String mId;

    private static final int CHAT_RIGHT = 1;
    private static final int CHAT_LEFT = 2;




    public MessengerRVA(Context context, List<Messages>  messagedata, String mID) {
        inflater = LayoutInflater.from(context);
        this.messagedata = messagedata;
        this.mId = mID;
    }

    @Override
    public int getItemViewType(int position) {
        if (messagedata.get(position).displayName.equals(mId))
            return CHAT_RIGHT;

        return CHAT_LEFT;
    }

    @Override
    public MessengerRVA.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_bubble_right, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_bubble_left, parent, false);
        }

        MyViewHolder viewHolder = new MyViewHolder(v,viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessengerRVA.MyViewHolder holder, int position) {
       Messages m =  messagedata.get(position);
        holder.txtMessage.setText( m.message);
        holder.txtTime.setText(m.timestamp);
        holder.DisplayName.setText(m.displayName);

    }

    @Override
    public int getItemCount() {
        return messagedata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtMessage;
        TextView txtTime;
        TextView DisplayName;



        public MyViewHolder(View itemView, int viewType ) {

            super(itemView);

            if (viewType == CHAT_RIGHT) {
                txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
                txtTime = (TextView) itemView.findViewById(R.id.txtTime);
                DisplayName = (TextView) itemView.findViewById(R.id.txtDis);

            }
            else
            {
                txtMessage = (TextView) itemView.findViewById(R.id.txtmessages);
                txtTime = (TextView) itemView.findViewById(R.id.txtTimes);
                DisplayName = (TextView) itemView.findViewById(R.id.txtDisplayname);

            }

        }

    }
}
