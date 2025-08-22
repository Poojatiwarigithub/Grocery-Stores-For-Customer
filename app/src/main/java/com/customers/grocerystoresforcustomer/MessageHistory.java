package com.customers.grocerystoresforcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.Model.Chats_Table;
import com.customers.grocerystoresforcustomer.Model.MessageAdapter;
import com.customers.grocerystoresforcustomer.Model.Shopper_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageHistory extends AppCompatActivity {

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date;
    CircleImageView shopper_profile_image;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databaseReference1, databaseReference2;
    TextView shopperName, status_on;
    Intent intent;
    String shopper_id;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    ArrayList<Chats_Table> mChat;
    RecyclerView recyclerView;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar_message_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        shopperName = findViewById(R.id.shopperName);
        shopper_profile_image = findViewById(R.id.shopper_profile_image);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        status_on = findViewById(R.id.status_on);

        recyclerView = findViewById(R.id.recycleview_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        shopper_id = intent.getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Message-Sending-Start
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), shopper_id, msg);
                } else {
                    Toast.makeText(MessageHistory.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Shopper-Registration-Details").child(shopper_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shopper_Details shopper_details = dataSnapshot.getValue(Shopper_Details.class);

                shopperName.setText(shopper_details.getShoppersname());
                status_on.setVisibility(View.VISIBLE);
                status_on.setText(shopper_details.getStatus());

                if (shopper_details.getImageURL().equals("default")) {
                    shopper_profile_image.setImageResource(R.drawable.man);
                } else {
                    Glide.with(getApplicationContext()).load(shopper_details.getImageURL()).into(shopper_profile_image);
                }
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                readMessage(firebaseUser.getUid(), shopper_id, shopper_details.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(shopper_id);

    }

    private void seenMessage(final String userid) {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats_Table chats_table = snapshot.getValue(Chats_Table.class);
                    if (chats_table.getReceiver().equals(firebaseUser.getUid()) && chats_table.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message) {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("h:mm a, dd MMM");
        Date = simpleDateFormat.format(calendar.getTime());
        Date = Date.replace("AM", "am").replace("PM", "pm");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("sendingtime", Date);
        reference.child("Chats").push().setValue(hashMap);

    }

    public void readMessage(final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats_Table chats_table = snapshot.getValue(Chats_Table.class);

                    if (chats_table.getReceiver().equals(myid) && chats_table.getSender().equals(userid) ||
                            chats_table.getReceiver().equals(userid) && chats_table.getSender().equals(myid)) {
                        mChat.add(chats_table);
                    }

                    messageAdapter = new MessageAdapter(MessageHistory.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    protected void onPause() {
        super.onPause();
        databaseReference1.removeEventListener(seenListener);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "offline");
        databaseReference2.updateChildren(hashMap);

    }

    protected void onResume() {
        super.onResume();

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "online");
        databaseReference2.updateChildren(hashMap);

    }

    protected void onStart() {
        super.onStart();

        databaseReference2 = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "online");
        databaseReference2.updateChildren(hashMap);

    }

    public void onBackPressed() {
        finish();
    }
}
