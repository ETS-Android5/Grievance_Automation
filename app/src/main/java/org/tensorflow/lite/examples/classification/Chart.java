package org.tensorflow.lite.examples.classification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {
    Float Ph,Ws,Wd,Sp;
    ArrayList<PieEntry> vc=new ArrayList<>();
    ArrayList<Float> v=new ArrayList<>();
    PieChart pc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ArrayList<BarEntry> vehiclecount=new ArrayList<>();
        pc=findViewById(R.id.barchart);


       DatabaseReference dbreforcountincrement= FirebaseDatabase.getInstance().getReference("GrievanceCount");
        dbreforcountincrement.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                     Ph=Float.parseFloat(snapshot.child("0 potholes").getValue().toString());
                     Sp=Float.parseFloat(snapshot.child("1 sewage pipeline").getValue().toString());
                    Wd=Float.parseFloat(snapshot.child("2 wastage dump").getValue().toString());
                    Ws=Float.parseFloat(snapshot.child("3 water stagnant").getValue().toString());


                vc.add(new PieEntry(Ph,"Potholes"));
                vc.add(new PieEntry(Sp,"Sewage Pipeline"));
                vc.add(new PieEntry(Wd,"Wastage Dump"));

                vc.add(new PieEntry(Ws,"Water Stagnant"));
                PieDataSet pieDataSet =new PieDataSet(vc,"Grievances");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData=new PieData(pieDataSet);
                pc.setData(pieData);
                pc.getDescription().setEnabled(true);

                pc.setCenterText("Grievance Count");
                pc.animate();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}