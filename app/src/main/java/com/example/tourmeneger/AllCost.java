package com.example.tourmeneger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AllCost extends AppCompatActivity {


    TextView tvResult;
    EditText etNum1, etNum2, etNum3, etNum4, etNum5, etNum6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cost);

        tvResult = findViewById(R.id.tvResult);
        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        etNum3 = findViewById(R.id.etNum3);
        etNum4 = findViewById(R.id.etNum4);
        etNum5 = findViewById(R.id.etNum5);
        etNum6 = findViewById(R.id.etNum6);
    }

    public void addNumber(View v){
        double n1,n2,n3,n4,n5,n6,result,result2;

        n1 = Double.parseDouble(etNum1.getText().toString());
        n2 = Double.parseDouble(etNum2.getText().toString());
        n3 = Double.parseDouble(etNum3.getText().toString());
        n4 = Double.parseDouble(etNum4.getText().toString());
        n5 = Double.parseDouble(etNum5.getText().toString());
        n6 = Double.parseDouble(etNum6.getText().toString());

        result = ((((n6*n5)/100)*n4)/n1) + ((n3*n2)/n1);

        tvResult.setText(String.valueOf(result));
    }
}
