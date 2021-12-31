package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.examples.classification.tflite.Classifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.tensorflow.lite.examples.classification.Number.nameuser;
import static org.tensorflow.lite.examples.classification.Number.numberuser;

public class Upload extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    ImageView imageView;
    String mCurrentPhotoPath;
    private final Integer CAMIMAGE=123;
    int mcount;
    private StorageReference Folder;
    Uri mPhotoUri;
    String imgurl, numberstring;
    private static final int REQUEST_LOCATION = 1;

    protected String latitude, longitude;
    protected LocationManager locationManager;
    Uri filePath;


    protected boolean gps_enabled, network_enabled;
    private static final int Imageback = 1;
    String lat,lon;
    String provider;
    String classifier,cclassifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


       /*ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);*/
        //Folder = FirebaseStorage.getInstance().getReference().child("ImageFolder");


       /* Button upload = findViewById(R.id.buttonupload);
        imageView = (ImageView) this.findViewById(R.id.imageView1);*/
        TextView tv = findViewById(R.id.textid);
        TextView number = findViewById(R.id.num);
         classifier = getIntent().getStringExtra("classification");
         int n=classifier.length();
        String m=classifier.substring(2,n);
         cclassifier = "The grievance in your area is found to be " + m + ". We will get back to you sooner. Thank you...";
        tv.setText(cclassifier);
        Toast.makeText(Upload.this, "Loading...", Toast.LENGTH_SHORT).show();
        getCount(classifier);


        getLocation();


        LocationManager nManager = (LocationManager) getSystemService(Upload.LOCATION_SERVICE);

        Location locationGPS = nManager.getLastKnownLocation(nManager.GPS_PROVIDER);
















    }
    public void getCount(String datachild){

        DatabaseReference dbcount = FirebaseDatabase.getInstance().getReference("GrievanceCount");
        dbcount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int Count=Integer.parseInt(snapshot.child(datachild).getValue().toString());
                DatabaseReference dbci = FirebaseDatabase.getInstance().getReference("GrievanceCount").child(datachild);
                dbci.setValue(++Count);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Upload.this,Homescreen.class));
    }

    private void upload(){
        numberstring = numberuser;

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Grievances").child(classifier).child("+91" + numberstring);
            db.child("Classifier").setValue(classifier);
             db.child("Name").setValue(nameuser);
            db.child("Lat").setValue(lat);
            db.child("Long").setValue(lon);
            db.child("Number").setValue("+91" + numberstring);


            Toast.makeText(Upload.this, "Posted Successfully", Toast.LENGTH_SHORT).show();

            moveback();


    }
    private void moveback(){

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+91"+numberstring, null, cclassifier, null, null);


        }
        catch (Exception e){
            Toast.makeText(this, "Allow permission for SMS", Toast.LENGTH_SHORT).show();
        }


    }
    private void getLocation() {
        Toast.makeText(Upload.this, "Uploading...", Toast.LENGTH_LONG).show();
        LocationRequest locationRequest= new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(Upload.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(Upload.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size()>0){
                            int latestlocationindex = locationResult.getLocations().size()-1;
                            double latitude= locationResult.getLocations().get(latestlocationindex).getLatitude();
                            double longitude= locationResult.getLocations().get(latestlocationindex).getLongitude();
                            lat=Double.toString(latitude);
                            lon=Double.toString(longitude);
                            upload();


                        }
                    }
                }, Looper.getMainLooper());

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(Upload.this, " "+filePath, Toast.LENGTH_SHORT).show();




/*
            final StorageReference Imagename = Folder.child("image" + ImageData.getLastPathSegment());




            Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {

                            imgurl=String.valueOf(uri);
                            Toast.makeText(Upload.this, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();






                        }
                    });
                }
            });*/


    }
}