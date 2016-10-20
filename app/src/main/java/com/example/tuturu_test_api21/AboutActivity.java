package com.example.tuturu_test_api21;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

//Активность окна "о программе"
public class AboutActivity extends AppCompatActivity {

    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Отобразить версию приложения
        version = (TextView) findViewById(R.id.versionLabel);
        String tmp ="Версия " + BuildConfig.VERSION_NAME;
        version.setText(tmp);

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
