package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class Number extends AppCompatActivity {
    public static String numberuser,nameuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        TextInputEditText number=findViewById(R.id.num);
        TextInputEditText name=findViewById(R.id.name);
        Button next = findViewById(R.id.buttonupload);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                numberuser=number.getText().toString();
                nameuser=name.getText().toString();
                if(numberuser.equals("")||nameuser.equals("")){
                    Toast.makeText(Number.this, "Please enter the proper credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                startActivity(new Intent(Number.this,ClassifierActivity.class));
                }
            }
        });
    }
}