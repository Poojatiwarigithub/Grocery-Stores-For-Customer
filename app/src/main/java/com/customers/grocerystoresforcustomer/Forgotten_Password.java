package com.customers.grocerystoresforcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Forgotten_Password extends AppCompatDialogFragment {

    EditText email_id;
    Button Cancelbtnforemailforgot,Updatebtnforemailforgot;
    LinearLayout forgot_password_linear_layout,updatecontact_linearlayout;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder changebuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View changeview = inflater.inflate(R.layout.dialogbox_update_contact_and_emailverification, null);
        Updatebtnforemailforgot = changeview.findViewById(R.id.Updatebtnforemailforgot);
        Cancelbtnforemailforgot = changeview.findViewById(R.id.Cancelbtnforemailforgot);
        email_id = changeview.findViewById(R.id.email_id);
        forgot_password_linear_layout = changeview.findViewById(R.id.forgot_password_linear_layout);
        updatecontact_linearlayout = changeview.findViewById(R.id.updatecontact_linearlayout);
        progressBar = changeview.findViewById(R.id.progresbar_email);

        updatecontact_linearlayout.setVisibility(View.GONE);
        forgot_password_linear_layout.setVisibility(View.VISIBLE);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        changebuilder.setView(changeview);


        Updatebtnforemailforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email_id = email_id.getText().toString().trim();

                if (TextUtils.isEmpty(txt_email_id)) {

                    Toast.makeText(getActivity(), "Please Enter Email!", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);


                FirebaseAuth.getInstance().sendPasswordResetEmail(txt_email_id)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    email_id.setText("");
                                    Toast.makeText(getActivity(), "Password reset link Sent", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    getDialog().dismiss();

                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "We haven't any account with this email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                }
            }
        });

        Cancelbtnforemailforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return changebuilder.show();
    }
}