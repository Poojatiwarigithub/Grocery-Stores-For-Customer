package com.customers.grocerystoresforcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.customers.grocerystoresforcustomer.Fragments.CustomerDetailsFragment;
import com.customers.grocerystoresforcustomer.Fragments.NewOrderBookCustFragment;
import com.customers.grocerystoresforcustomer.Fragments.OrderHistoryForCustomersFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Customer_Home extends AppCompatActivity {
    CircleImageView customer_profile_image;
    TextView cutomer_name;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__home);

        tabLayout = findViewById(R.id.customer_home_tablayout);
        viewPager = findViewById(R.id.customer_home_viewpager);
        customer_profile_image = findViewById(R.id.customer_profile_image);
        cutomer_name = findViewById(R.id.customer_name);
        Toolbar toolbar = findViewById(R.id.cutomer_home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grocery Stores");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","online");
        databaseReference.updateChildren(hashMap);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragement(new NewOrderBookCustFragment(), "Book New Order");
        viewPagerAdapter.addFragement(new OrderHistoryForCustomersFragment(), "Order History");
        viewPagerAdapter.addFragement(new CustomerDetailsFragment(), "Profile Details");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status","offline");
                FirebaseDatabase.getInstance().getReference("Customers-Registration-details").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);

               Intent intent = new Intent(Customer_Home.this,CustomerLogin.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
               return true;

            case R.id.update_contact:
                Update_Contact_Number update_contact_number  = new Update_Contact_Number();
                update_contact_number.show(getSupportFragmentManager(),"Update Contact Number");
                return true;

            case R.id.delete_account:

                Delete_Account delete_account  = new Delete_Account();
                delete_account.show(getSupportFragmentManager(),"Delete Account");
                return true;

            case R.id.change_password:
                ChangePassword changePassword = new ChangePassword();
                changePassword.show(getSupportFragmentManager(),"Change Password");
                return true;

        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter (FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragement (Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }
    }

    protected void onPause(){
        super.onPause();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","offline");
        FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
    }
    protected void onResume(){
        super.onResume();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status","online");
        FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
    }

    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Click two times to close an activity", Toast.LENGTH_SHORT).show(); }
        mBackPressed = System.currentTimeMillis();
    }

}