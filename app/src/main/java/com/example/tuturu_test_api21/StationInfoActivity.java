package com.example.tuturu_test_api21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class StationInfoActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);


        Intent intent = getIntent();

        //Полчуение данных о станции
        String infoStation = intent.getStringExtra(MainActivity.MESSAGE_STATION);
        String  infoCountry = intent.getStringExtra(MainActivity.MESSAGE_COUNTRY);
        String infoCity = intent.getStringExtra(MainActivity.MESSAGE_CITY);
        String infoRegion = intent.getStringExtra(MainActivity.MESSAGE_REGION);

        TextView stationText = (TextView) findViewById(R.id.infoStationTitle);
        TextView countryText = (TextView) findViewById(R.id.infoCountryTitle);
        TextView cityText = (TextView) findViewById(R.id.infoCityTitle);
        TextView regionText = (TextView) findViewById(R.id.infoRegionTitle);
        TextView regionLabel = (TextView) findViewById(R.id.infoRegionLabel);

        if(infoRegion.equals(""))
            regionLabel.setText("");

        stationText.setText(infoStation);
        countryText.setText(infoCountry);
        cityText.setText(infoCity);
        regionText.setText(infoRegion);

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
