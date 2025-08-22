package com.customers.grocerystoresforcustomer.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.MessageHistory;
import com.customers.grocerystoresforcustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShopperMessagesAdapter extends RecyclerView.Adapter<ShopperMessagesAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Shopper_Details> shoplist;
    boolean isChat;
    String theLastMessage;

    public ShopperMessagesAdapter(Context mContext, ArrayList<Shopper_Details> shoplist, boolean isChat)
    {
        this.shoplist = shoplist;
        this.mContext=mContext;
        this.isChat = isChat;
    }
    @NonNull
    @Override
    public ShopperMessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_list_items,parent,false);
        return new ShopperMessagesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopperMessagesAdapter.MyViewHolder holder, final int position) {
        final Shopper_Details shopper_details = shoplist.get(position);
        holder.ShopperName.setText(shopper_details.getShoppersname());
        holder.ShopName.setText(shopper_details.getShopName());


        if(shopper_details.getImageURL().equals("default")){
            holder.profile.setImageResource(R.drawable.man);

        }else
        {
            Glide.with(mContext).load(shopper_details.getImageURL()).into(holder.profile);
        }

        if(isChat){
            lastMessage(shopper_details.getId(),holder.Contact);
        }else {
            holder.Contact.setVisibility(View.GONE);

        }


        if(isChat){

            if(shopper_details.getStatus().equals("online")){
                holder.status_on.setVisibility(View.VISIBLE);
            }else {
                holder.status_on.setVisibility(View.GONE);
            }
        }else {
            holder.status_on.setVisibility(View.GONE);
        }

        //to display all customer incoming message
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageHistory.class);
                intent.putExtra("id",shopper_details.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                mContext.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return shoplist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
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

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "defaut";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats_Table chats_table = snapshot.getValue(Chats_Table.class);



                    if(chats_table.getReceiver().equals(firebaseUser.getUid()) && chats_table.getSender().equals(userid) ||
                            chats_table.getReceiver().equals(userid) && chats_table.getSender().equals(firebaseUser.getUid()) ){

                        theLastMessage = chats_table.message;
                        if(chats_table.getReceiver().equals(firebaseUser.getUid()) && !chats_table.isIsseen()){
                            last_msg.setTextSize(18);
                            last_msg.setTypeface(last_msg.getTypeface(), Typeface.BOLD);
                            last_msg.setTextColor(Color.parseColor("#D81B60"));

                        }
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                }
                theLastMessage = "default";

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

