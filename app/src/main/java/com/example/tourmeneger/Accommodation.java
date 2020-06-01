package com.example.tourmeneger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Accommodation extends AppCompatActivity {

    TextView tvResult;
    EditText etNum1, etNum2, etNum3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation);

        tvResult = findViewById(R.id.tvResult);
        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        etNum3 = findViewById(R.id.etNum3);
    }

    public void addNumber(View v){
        double n1,n2,n3,result;

        n1 = Double.parseDouble(etNum1.getText().toString());
        n2 = Double.parseDouble(etNum2.getText().toString());
        n3 = Double.parseDouble(etNum3.getText().toString());

        result = (n3*n2)/n1;

        tvResult.setText(String.valueOf(result));
    }
}
