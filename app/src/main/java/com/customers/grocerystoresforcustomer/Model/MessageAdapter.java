package com.customers.grocerystoresforcustomer.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.customers.grocerystoresforcustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {


    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    Context mContext;
    ArrayList<Chats_Table> mChat;
    String imageurl;
    FirebaseUser firebaseUser;
    Date date,date1;
    String gettime;


    public MessageAdapter(Context mContext, ArrayList<Chats_Table> mChat, String imageurl)
    {
        this.mChat = mChat;
        this.mContext=mContext;
        this.imageurl=imageurl;
    }
    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.MyViewHolder(view);

        }
        else {
           View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, final int position) {
        Chats_Table chats_table = mChat.get(position);
        holder.show_message.setText(chats_table.getMessage());
        holder.show_message_time.setText(chats_table.getSendingtime());
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.man);
        }
        else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }
        if(position == mChat.size()-1){
            if(chats_table.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else {
                holder.txt_seen.setText("Delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message,txt_seen,show_message_time;
        public CircleImageView profile_image;
        public MyViewHolder(@NonNull View itemview){
            super(itemview);
            show_message = itemview.findViewById(R.id.show_message);
            txt_seen = itemview.findViewById(R.id.txt_seen);
            show_message_time = itemview.findViewById(R.id.show_message_time);
            profile_image = itemview.findViewById(R.id.profile_image);

        }
    }

    public int getItemViewType(int position){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }
}

