package com.talhaamin.parley;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.talhaamin.parley.Models.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by Talha Amin on 1/6/2017.
 */

public class UserListRVadapter extends RecyclerView.Adapter<UserListRVadapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<User> userdata = Collections.emptyList();


    public UserListRVadapter(Context context, List<User> userdata) {
        inflater = LayoutInflater.from(context);
        this.userdata = userdata;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        MyViewHolder viewholder = new MyViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final User user= userdata.get(position);
        holder.txtDisplayName.setText(user.displayName);
        holder.txtPhone.setText(user.userId);
        Picasso.with(holder.imgProfile.getContext())
                .load(user.pic_url)
                .into(holder.imgProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if(user.isSelected == false)
                {
                    holder.bkLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.list_selected));
                    holder.txtDisplayName.setTextColor(ContextCompat.getColor(view.getContext(),R.color.colorAccent));
                    holder.txtPhone.setTextColor(ContextCompat.getColor(view.getContext(),R.color.colorAccent));
                    user.isSelected = true;
                }
                else if (user.isSelected == true)
                {
                    holder.bkLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.list_background));
                    holder.txtDisplayName.setTextColor(ContextCompat.getColor(view.getContext(),R.color.text_color));
                    holder.txtPhone.setTextColor(ContextCompat.getColor(view.getContext(),R.color.text_color));
                    user.isSelected = false;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return userdata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDisplayName;
        TextView txtPhone;
        ImageView imgProfile;
        ConstraintLayout bkLayout;

        public MyViewHolder(View itemView) {

            super(itemView);



            txtDisplayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
            imgProfile = (ImageView) itemView.findViewById(R.id.imgProfile);
            bkLayout = (ConstraintLayout) itemView.findViewById(R.id.layout_background);
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
