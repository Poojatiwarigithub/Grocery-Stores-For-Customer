package com.customers.grocerystoresforcustomer.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.customers.grocerystoresforcustomer.Model.Customersdetails;
import com.customers.grocerystoresforcustomer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class DialogBoxForCustomersrDetailsUpdate extends AppCompatDialogFragment {
    private EditText Customer_Name;
    private Spinner customercity,customersubcity;
    private Button Updatebtn,Cancelbtn;
   // DialogBoxListener dialogBoxListener;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;
    private String txt_Customer_Name,txt_customercity,txt_customersubcity;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> subadapter;
    ArrayList<String> spinnerdatalist,spinnersubdatalist;
    String cityselected,cityget,subcityselected,subcityget;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogbox_updatecustomers_details,null);
        Customer_Name = view.findViewById(R.id.Customer_Name);
        customercity = view.findViewById(R.id.customercity);
        customersubcity = view.findViewById(R.id.customersubcity);
        Updatebtn = view.findViewById(R.id.Updatebtn);
        Cancelbtn = view.findViewById(R.id.Cancelbtn);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                Customer_Name.setText(customersdetails.getCustomerName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fetch city data from database
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Cities");
        databaseReference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerdatalist = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    cityselected = dataSnapshot1.getKey();
                    spinnerdatalist.add(cityselected);
                    Collections.sort(spinnerdatalist);

                }
                adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items,spinnerdatalist);
                adapter.setDropDownViewResource(R.layout.spinner_items);
                customercity.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        customercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityget = (String) parent.getItemAtPosition(position);

                databaseReference2 = FirebaseDatabase.getInstance().getReference("Cities").child(cityget);
                databaseReference2.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        spinnersubdatalist = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            subcityselected = dataSnapshot1.child("cityname").getValue(String.class);
                            spinnersubdatalist.add(subcityselected);
                            Collections.sort(spinnersubdatalist);

                        }
                        subadapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items,spinnersubdatalist);
                        subadapter.setDropDownViewResource(R.layout.spinner_items);
                        customersubcity.setAdapter(subadapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                customersubcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subcityget = (String) parent.getItemAtPosition(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        builder.setView(view);


        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_Customer_Name = Customer_Name.getText().toString();
                txt_customercity = cityget;
                txt_customersubcity = subcityget;

                if(TextUtils.isEmpty(txt_Customer_Name) || TextUtils.isEmpty(txt_customercity) )
                {
                    Toast.makeText(getActivity(),"All fileds are required!",Toast.LENGTH_SHORT).show();

                }
                else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("customerName", txt_Customer_Name);
                    map.put("customerCity", txt_customercity);
                    map.put("customerarelocation", txt_customersubcity);
                    databaseReference.updateChildren(map);
                    getDialog().dismiss();
                }

            }
        });

    return builder.show();
    }
}
