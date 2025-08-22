package com.customers.grocerystoresforcustomer.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.R;
import com.customers.grocerystoresforcustomer.Shopperdetails_for_customer;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    Context mContext;
    ArrayList<Shopper_Details> list;
    boolean isChat;

    public AdapterClass(Context mContext, ArrayList<Shopper_Details> list, boolean isChat)
    {
        this.list = list;
        this.mContext=mContext;
        this.isChat = isChat;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_list_items,parent,false);
        return new AdapterClass.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Shopper_Details user = list.get(position);
        holder.ShopperName.setText(user.getShoppersname());
        holder.ShopName.setText(user.getShopName());
        holder.Contact.setText(user.getShopperContact());


        if(user.getImageURL().equals("default")){
            holder.profile.setImageResource(R.drawable.man);

        }else
            {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile);
        }

        if(isChat){
            if(user.getStatus().equals("online")){
                holder.status_on.setVisibility(View.VISIBLE);
            }else {
                holder.status_on.setVisibility(View.GONE);
            }
        }else {
            holder.status_on.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Shopperdetails_for_customer.class);
                intent.putExtra("id", user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ShopperName,ShopName,Contact;
        CircleImageView profile, status_on;
        public MyViewHolder(@NonNull View itemview){
            super(itemview);
            ShopperName = itemview.findViewById(R.id.shopername);
            ShopName = itemview.findViewById(R.id.shopname);
            Contact = itemview.findViewById(R.id.contact);
            profile = itemview.findViewById(R.id.profile);
            status_on = itemview.findViewById(R.id.status_on);

        }
    }
}
