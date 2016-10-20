package com.example.tuturu_test_api21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CalendarView;

//Активность окна выбора даты
public class CalendarActivity extends AppCompatActivity {

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        calendar = (CalendarView) findViewById(R.id.calendarView);


        //Листнер для возвращения даты при выборе числа
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.MESSAGE_DAY,dayOfMonth);
                intent.putExtra(MainActivity.MESSAGE_MONTH,month);
                intent.putExtra(MainActivity.MESSAGE_YEAR,year);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }




}
