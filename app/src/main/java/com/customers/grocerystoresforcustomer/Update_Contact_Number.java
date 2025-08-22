package com.customers.grocerystoresforcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.customers.grocerystoresforcustomer.Model.Customersdetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Update_Contact_Number extends AppCompatDialogFragment {

    private TextView current_contact;
    private EditText new_contact;
    private Button Cancelbtn, Updatebtn;
    private ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference1;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder changebuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View changeview = inflater.inflate(R.layout.dialogbox_update_contact_and_emailverification, null);
        current_contact = changeview.findViewById(R.id.current_contact);
        new_contact = changeview.findViewById(R.id.new_contact);
        Cancelbtn = changeview.findViewById(R.id.Cancelbtn);
        Updatebtn = changeview.findViewById(R.id.Updatebtn);
        progressBar = changeview.findViewById(R.id.progresbar_contact);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        databaseReference1 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                current_contact.setText(customersdetails.getCustomerContact());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changebuilder.setView(changeview);


        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String txt_new_contact = new_contact.getText().toString();


                if (txt_new_contact.isEmpty() || txt_new_contact.length() < 10) {

                    Toast.makeText(getActivity(), "Please enter valid mobile number!", Toast.LENGTH_SHORT).show();
                }

                else {

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference1.child("customerContact").setValue(txt_new_contact);
                    getDialog().dismiss();
                    Toast.makeText(getActivity(), "Mobile  Number updated successfully.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
           }
        });


        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return changebuilder.show();
    }
}