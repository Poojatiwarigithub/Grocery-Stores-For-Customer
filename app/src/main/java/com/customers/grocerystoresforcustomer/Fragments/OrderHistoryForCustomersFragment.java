package com.customers.grocerystoresforcustomer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customers.grocerystoresforcustomer.Model.Chats_Table;
import com.customers.grocerystoresforcustomer.Model.ShopperMessagesAdapter;
import com.customers.grocerystoresforcustomer.Model.Shopper_Details;
import com.customers.grocerystoresforcustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class OrderHistoryForCustomersFragment extends Fragment {
    RecyclerView recyclerView;
    ShopperMessagesAdapter shopperMessagesAdapter;

    ArrayList<Shopper_Details> shoplist;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,databaseReference1;
    ArrayList<String> shoppersList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history_for_customers, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_shoppers_history_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        shoppersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats_Table chats_table = snapshot.getValue(Chats_Table.class);

                    if(chats_table.getSender().equals(firebaseUser.getUid())){
                        shoppersList.add(chats_table.getReceiver());
                    }
                    HashSet hs = new HashSet();
                    hs.addAll(shoppersList);
                    shoppersList.clear();
                    shoppersList.addAll(hs);
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
    public void readChats(){
        shoplist = new ArrayList<>();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoplist.clear();
                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    Shopper_Details shopper_details = snapshot1.getValue(Shopper_Details.class);
                    for(String id : shoppersList) {
                        if(shopper_details.getId().equals(id)){
                            shoplist.add(shopper_details);
                        }
                    }
                }
                shopperMessagesAdapter = new ShopperMessagesAdapter(getContext(),shoplist,true);
                recyclerView.setAdapter(shopperMessagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
