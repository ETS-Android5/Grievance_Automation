package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.security.AccessController.getContext;

public class Grievances extends AppCompatActivity {
    DatabaseReference databaseReference;
    FloatingActionButton addevent;
    ChipGroup chipGroup;
    private  RecyclerView meRecyclerView;
    public Grievances(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievances);
        meRecyclerView=findViewById(R.id.rvforgrievances);
        meRecyclerView.setLayoutManager(new LinearLayoutManager(Grievances.this));
        chipGroup=(ChipGroup) findViewById(R.id.chipgrp);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                Chip chip=chipGroup.findViewById(checkedId);


                    String typeg=chip.getText().toString();
                databaseReference= FirebaseDatabase.getInstance().getReference("Grievances").child(typeg);
                database();









            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        databaseReference= FirebaseDatabase.getInstance().getReference("Grievances").child("1 sewage pipeline");
        database();





    }
    public void  database(){ FirebaseRecyclerOptions<modelgrievances> options=new FirebaseRecyclerOptions.Builder<modelgrievances>()
            .setQuery(databaseReference,modelgrievances.class).build();
        FirebaseRecyclerAdapter<modelgrievances,Notificationsfragmentvh> adapter=new FirebaseRecyclerAdapter<modelgrievances, Notificationsfragmentvh>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Notificationsfragmentvh notificationsfragmentvh, int i, @NonNull modelgrievances mg) {
                notificationsfragmentvh.setDetails(Grievances.this,mg.getNumber(),mg.getClassifier(),mg.getLat(),mg.getLong(),mg.getName());



            }

            @NonNull
            @Override
            public Notificationsfragmentvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grievancesrow,parent,false);

                Notificationsfragmentvh notificationsfragmentvh=   new Notificationsfragmentvh(view);
                return notificationsfragmentvh;
            }
        };
        meRecyclerView.setAdapter(adapter);
        adapter.startListening();










    }
    public  class Notificationsfragmentvh extends RecyclerView.ViewHolder{
        TextView Numview,typeview;
        ImageView navigate;
        Button clear;






        public Notificationsfragmentvh(@NonNull View itemView) {
            super(itemView);




            Numview=itemView.findViewById(R.id.Number);
            typeview=itemView.findViewById(R.id.Type);
            navigate=itemView.findViewById(R.id.navigate);
            clear=itemView.findViewById(R.id.cleargrievance);






        }

        public void setDetails(final Context ctx, final String number,final String type,final String la,final String lo,final String name)  {







            int n=type.length();
            String m=type.substring(2,n);
            Numview.setText("Posted by "+name);
            typeview.setText(m);


            double latitude,longitude;
            latitude = Double.parseDouble(la);
            longitude = Double.parseDouble(lo);
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+latitude+","+longitude+"&mode=1"));
                    i.setPackage("com.google.android.apps.maps");
                    startActivity(i);
                }
            });
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Grievances").child(type).child(number);
                   dbref.removeValue();
                    try{
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, "Hi "+name+", the grievance in your area has been cleared, please do verify", null, null);
                        Toast.makeText(Grievances.this, "Grievances cleared and message sent to respective user",
                                Toast.LENGTH_LONG).show();

                    }
                    catch (Exception e){
                        Toast.makeText(ctx, "Allow permission for SMS", Toast.LENGTH_SHORT).show();
                    }
                }
            });























        }

    }
}