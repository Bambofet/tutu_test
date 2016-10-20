package com.example.tuturu_test_api21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String MESSAGE_DAY = "com.example.tutu_test_app.city";
    public final static String MESSAGE_MONTH = "com.example.tutu_test_app.station";
    public final static String MESSAGE_YEAR = "com.example.tutu_test_app.region";
    public final static String MESSAGE_CITY = "com.example.tutu_test_app.city";
    public final static String MESSAGE_STATION = "com.example.tutu_test_app.station";
    public final static String MESSAGE_REGION = "com.example.tutu_test_app.region";
    public final static String MESSAGE_COUNTRY = "com.example.tutu_test_app.country";
    public final static String MESSAGE_TO = "com.example.tutu_test_app.to";
    public final static String MESSAGE_FROM = "com.example.tutu_test_app.from";
    public final static int REQUEST_CODE0 = 100;
    public final static int REQUEST_CODE1 = 101;
    public final static int REQUEST_CODE2 = 102;


    public static String jsonData="";

    Station stationFrom;
    Station stationTo;

    TextView startText;
    TextView endText;

    TextView dateText;

    int day;
    int month;
    int year;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startText = (TextView) findViewById(R.id.startPoint);
        endText = (TextView) findViewById(R.id.destPoint);
        dateText = (TextView) findViewById(R.id.departDate);


    }


//Выбор даты отправления
    public void selectDate(View view)
    {
    Intent intent = new Intent(this, CalendarActivity.class);

        startActivityForResult(intent,REQUEST_CODE0);

    }



    //Выбор станции отправления
    public void selectStationFrom(View view)
    {
        Intent intent = new Intent(this, SelectStationActivity.class);
       // startActivity(intent);]
        intent.putExtra(MESSAGE_TO,true);
        intent.putExtra(MESSAGE_FROM,false);
        startActivityForResult(intent,REQUEST_CODE1);
    }

    public void selectStationTo(View view)
    {
        Intent intent = new Intent(this, SelectStationActivity.class);
        // startActivity(intent);]
        intent.putExtra(MESSAGE_TO,false);
        intent.putExtra(MESSAGE_FROM,true);
        startActivityForResult(intent,REQUEST_CODE2);
    }



    //Получение выбранной станции
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE1) //Обработка начальной точки
        {
            if(resultCode == RESULT_OK)
            {
                stationFrom = new Station();
                stationFrom.stationTitle = data.getStringExtra(MESSAGE_STATION);
                String tmpString = getResources().getString(R.string.startPoint);
                tmpString = tmpString + System.lineSeparator() + stationFrom.stationTitle;
                startText.setText(tmpString);
            }
        }
        else
        if (requestCode == REQUEST_CODE2) //Обработка Конечной точки
        {
            if(resultCode == RESULT_OK)
            {
                stationTo = new Station();
                stationTo.stationTitle = data.getStringExtra(MESSAGE_STATION);
                String tmpString = getResources().getString(R.string.destPoint);
                tmpString = tmpString + System.lineSeparator() + stationTo.stationTitle;
                endText.setText(tmpString);
            }
        }
        else
        if (requestCode == REQUEST_CODE0) //Обработка выбранной даты
        {
            if(resultCode == RESULT_OK)
            {

                day = data.getIntExtra(MESSAGE_DAY,0);
                month = data.getIntExtra(MESSAGE_MONTH,0)+1;
                year = data.getIntExtra(MESSAGE_YEAR,0);
                String tmpString= getResources().getString(R.string.departDate);
                tmpString = tmpString + System.lineSeparator() + day + "-" +  month + "-" + year;
                dateText.setText(tmpString);
            }
        }


    }




    //Кнопка "о программе"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.aboutOption)
        {
            Intent intent = new Intent(this, AboutActivity.class );
            startActivity(intent);
        }
    return super.onOptionsItemSelected(item);
    }






}
