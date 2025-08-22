package com.customers.grocerystoresforcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.customers.grocerystoresforcustomer.Model.PasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePassword  extends AppCompatDialogFragment {

    private EditText current_password,new_password,confirm_password;
    private Button Cancelbtn,Updatebtn;
    private ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    PasswordValidator passwordValidator;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder changebuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View changeview = inflater.inflate(R.layout.dialogbox_change_password, null);

        current_password = changeview.findViewById(R.id.current_password);
        new_password = changeview.findViewById(R.id.new_password);
        confirm_password = changeview.findViewById(R.id.confirm_password);
        Cancelbtn = changeview.findViewById(R.id.Cancelbtn);
        Updatebtn = changeview.findViewById(R.id.Updatebtn);
        progressBar= changeview.findViewById(R.id.progresbar_change_password);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        passwordValidator = new PasswordValidator();

        changebuilder.setView(changeview);


        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_current_password = current_password.getText().toString();
                final String s_new_password = new_password.getText().toString();
                String s_confirm_password = confirm_password.getText().toString();

                if (TextUtils.isEmpty(s_current_password) || TextUtils.isEmpty(s_new_password) || TextUtils.isEmpty(s_confirm_password)) {
                    Toast.makeText(getActivity(), "All fileds are required!", Toast.LENGTH_SHORT).show();
                }

                else if (!passwordValidator.validate(s_new_password)) {
                    Toast.makeText(getActivity(),"Password Must be 8-15 alphanumeric.",Toast.LENGTH_SHORT).show();
                }

                else if (s_new_password.equals(s_confirm_password)) {
                    progressBar.setVisibility(View.VISIBLE);
                    String email = firebaseUser.getEmail();
                    firebaseAuth.signInWithEmailAndPassword(email, s_current_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                firebaseUser.updatePassword(s_new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                                            getDialog().dismiss();
                                            startActivity(new Intent(getActivity(), CustomerLogin.class));


                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });

                }
                else {
                    Toast.makeText(getActivity(), "password and confirm password should be same!", Toast.LENGTH_SHORT).show();
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