package com.customers.grocerystoresforcustomer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.customers.grocerystoresforcustomer.Model.AdapterClass;
import com.customers.grocerystoresforcustomer.Model.Customersdetails;
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

public class NewOrderBookCustFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    AdapterClass adapterClass;
    DatabaseReference  databaseReference1;
    FirebaseUser firebaseUser;
    ArrayList<Shopper_Details> list;
    String getcutomercity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_order_book_cust, container, false);
        recyclerView = view.findViewById(R.id.recylerview_show_shopper);
        searchView = view.findViewById(R.id.search_bar_to_search_shoppers);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        list = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                getcutomercity = customersdetails.getCustomerarelocation();
                readShoppersDetails();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
    private void readShoppersDetails() {

        databaseReference1 = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                list.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Shopper_Details shopper_details = dataSnapshot1.getValue(Shopper_Details.class);

                        if (shopper_details.getShopsubcity().equals(getcutomercity)) {

                            list.add(shopper_details);

                        }

                    }
                adapterClass = new AdapterClass(getActivity(), list, true);
                recyclerView.setAdapter(adapterClass);

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return false;
                }
            });
        }
    }
    public void search(String str)
    {
        ArrayList<Shopper_Details> mylist = new ArrayList<>();
        for(Shopper_Details object : list)
        {
            if(object.getShopName().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }
            else if(object.getShoppersname().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }

            else if(object.getShopAddress().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }
            else if(object.getShoplandmark().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }
            else if(object.getShopPincode().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }

        }
        adapterClass = new AdapterClass(getContext(),mylist,true);
        recyclerView.setAdapter(adapterClass);
    }
}

