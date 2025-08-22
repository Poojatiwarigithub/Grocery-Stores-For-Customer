package com.customers.grocerystoresforcustomer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.bumptech.glide.Glide;
import com.customers.grocerystoresforcustomer.Model.Customersdetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;


public class ImageSelectUpload extends AppCompatActivity {

    Toolbar image_toolbar;
    ImageView imageview;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Uri resultUri;
    private StorageTask uploadTask;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    AnstronCoreHelper coreHelper;
    String oldprofilepic;
    Intent intent;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageselect);

        image_toolbar = findViewById(R.id.image_toolbar);
        imageview = findViewById(R.id.imageview);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        int deviceWidth = displayMetrics.widthPixels;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(deviceWidth, deviceWidth);

        imageview.setLayoutParams(lp);

        setSupportActionBar(image_toolbar);
        getSupportActionBar().setTitle("Profile photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       image_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Customers-Profile-Image");
        coreHelper = new AnstronCoreHelper(getApplicationContext());


        databaseReference = FirebaseDatabase.getInstance().getReference("Customers-Registration-details").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customersdetails customersdetails = dataSnapshot.getValue(Customersdetails.class);
                if (customersdetails.getImageURL().equals("default")) {
                    oldprofilepic = customersdetails.getImageURL();
                    imageview.setImageResource(R.drawable.man);
                } else {
                    oldprofilepic = customersdetails.getImageURL();
                    Glide.with(getApplicationContext()).load(customersdetails.getImageURL()).into(imageview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_image:
                openImage();
                imageUri = null;
                resultUri =null;
                return true;
        }
        return false;
    }


    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropMenuCropButtonTitle("SAVE")
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.getUri();
                uploadImage();
            }
        }
    }

    public void uploadImage() {


        final ProgressDialog progressDialog = new ProgressDialog(ImageSelectUpload.this);
        progressDialog.setMessage("Updating..");
        progressDialog.setCanceledOnTouchOutside(false);
        if (resultUri != null) {
            progressDialog.show();

            final File file = new File(SiliCompressor.with(getApplicationContext()).compress(FileUtils.getPath(
                    getApplicationContext(), resultUri), new File(getApplicationContext().getCacheDir(), "temp")));

            final Uri uri = Uri.fromFile(file);

            final StorageReference fileReference = storageReference.child(coreHelper.getFileNameFromUri(uri));

            uploadTask = fileReference.putFile(uri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            String mUri = downloadUri.toString();
                            if(!oldprofilepic.equals("default")){
                                StorageReference photodelete = FirebaseStorage.getInstance().getReferenceFromUrl(oldprofilepic);
                                photodelete.delete();
                                oldprofilepic = "";
                            }
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL", mUri);
                            databaseReference.updateChildren(map);



                            progressDialog.dismiss();
                            Toast.makeText(ImageSelectUpload.this, "Profile Uploaded Successfully!. ", Toast.LENGTH_SHORT).show();
                            finishActivity(Activity.RESULT_OK);

                    }
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }

    }
    /*public void onBackPressed(){
        *//*Intent intent = new Intent(ImageSelectUpload.this,Customer_Home.class);
        startActivity(intent);*//*
        ImageSelectUpload.this.finish();
    }*/
}