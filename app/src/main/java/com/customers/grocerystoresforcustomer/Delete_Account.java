package com.customers.grocerystoresforcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Delete_Account extends AppCompatDialogFragment {

    private TextView current_email;
    private EditText password;
    private Button Cancelbtn, Updatebtn;
    private ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference1;
    String oldprofilepic;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder changebuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View changeview = inflater.inflate(R.layout.dialogbox_delete_account, null);
        current_email = changeview.findViewById(R.id.current_email);
        password = changeview.findViewById(R.id.password);
        Cancelbtn = changeview.findViewById(R.id.Cancelbtn);
        Updatebtn = changeview.findViewById(R.id.Updatebtn);
        progressBar = changeview.findViewById(R.id.progresbar_deleteaccount);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        databaseReference1 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                current_email.setText(customersdetails.getCustomerEmailid());
                oldprofilepic = customersdetails.getImageURL();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changebuilder.setView(changeview);

        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String txt_email_id = current_email.getText().toString();
                final String txt_password = password.getText().toString();


                if (txt_password.isEmpty()) {

                    Toast.makeText(getActivity(), "Please enter your current password!", Toast.LENGTH_SHORT).show();
                }

                else {

                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(txt_email_id, txt_password);

                    firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                    firebaseUser.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        databaseReference1.removeValue();
                                                        if(!oldprofilepic.equals("default")){
                                                            StorageReference photodelete = FirebaseStorage.getInstance().getReferenceFromUrl(oldprofilepic);
                                                            photodelete.delete();
                                                            oldprofilepic = "";
                                                        }
                                                        Intent intent = new Intent(getContext(),CustomerLogin.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        Toast.makeText(getContext(),"Account Deleted Successfully!",Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });

                                }else  {
                                    Toast.makeText(getContext(),"Please Enter valid password!",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
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