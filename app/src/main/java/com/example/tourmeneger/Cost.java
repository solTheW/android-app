package com.example.tourmeneger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cost extends AppCompatActivity {

    private Button button1,button2,button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        button1 = (Button)findViewById(R.id.btn1);
        button2 = (Button)findViewById(R.id.btn2);
        button3 = (Button)findViewById(R.id.btn3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFuel();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccommodation();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCostall();
            }
        });
    }
    private void openFuel(){
        Intent intent = new Intent(this, Fuel.class);
        startActivity(intent);
    }
    private void openAccommodation(){
        Intent intent = new Intent(this, Accommodation.class);
        startActivity(intent);
    }
    private void openCostall(){
        Intent intent = new Intent(this, AllCost.class);
        startActivity(intent);
    }
}
