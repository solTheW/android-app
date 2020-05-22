package com.example.tourmeneger;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class KalendarzActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalendarz);

        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view);
    }
}
