package com.example.tuturu_test_api21;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SelectStationActivity extends AppCompatActivity {

    public ListView listView;
    public ArrayList<Station> rawStations = new ArrayList<>();  //Содержит все станции из JSON
    public ArrayList<Station> stations = new ArrayList<Station>();  //Содержит все станции, удовлетворяющие поиску
    public ListAdapter adapter;

    public LinearLayout errorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        errorLayout = (LinearLayout) findViewById(R.id.errorLayout);

        //Загрузка JSON из локального файла
     /*   InputStream is = getResources().openRawResource(R.raw.all_stations);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try
        {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n=reader.read(buffer)) != -1 )
            {
             writer.write(buffer,0,n);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
*/

        //Определение адаптера списка
         listView = (ListView) findViewById(R.id.stationsList);
         adapter = new ListAdapter(this);
         listView.setAdapter(adapter);


        //Включаем стрелку возвращения
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Если файл не загружен - загрузить
        if(MainActivity.jsonData.equals(""))
        new Download().execute();
        else
            getData(MainActivity.jsonData);


    }



    //Поиск станции
    void filter(String stringFilter)
    {
        stringFilter = stringFilter.toLowerCase();
        stations.clear();
        for (int i=0; i<rawStations.size();i++)
        {
        if (rawStations.get(i).stationTitle.toLowerCase().contains(stringFilter)
                || rawStations.get(i).cityTitle.toLowerCase().contains(stringFilter)
                || rawStations.get(i).countryTitle.toLowerCase().contains(stringFilter)
                || rawStations.get(i).regionTitle.toLowerCase().contains(stringFilter))
            stations.add(rawStations.get(i));
        }

        String countryHeader="";
        String cityHeader="";

        //Снятие старых заголовков списка и установка новых
        for(int i=0; i<stations.size();i++)
        {
            Station tempStation = stations.get(i);
            //Сброс всех меток заголовков стран
            if (tempStation.countryHeader)
                tempStation.countryHeader=false;
            if(!tempStation.countryTitle.equals(countryHeader)) //найти новые заголовки
            {
                tempStation.countryHeader=true;
                countryHeader=tempStation.countryTitle;
            }

            //То же самое с городами
            if (tempStation.cityHeader)
                tempStation.cityHeader=false;
            if(!tempStation.cityTitle.equals(cityHeader)) //найти новые заголовки
            {
                tempStation.cityHeader=true;
                cityHeader=tempStation.cityTitle;
            }

        }

        adapter.notifyDataSetChanged();
    }


    //Описание поля поиска
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }



    //Возвращение в главную активность
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    //Получить данные из JSON
    public void getData(String data)
    {
        if(MainActivity.jsonData.equals(""))
        {
            errorLayout.setVisibility(View.VISIBLE);
            return;
        }
        else
            errorLayout.setVisibility(View.INVISIBLE);
        //Получение данных из JSON


        try
        {
            JSONObject dataJsonOnj = new JSONObject(data);
            JSONArray citiesFrom = dataJsonOnj.getJSONArray("citiesFrom");//Маассив citiesFrom
            JSONArray citiesTo = dataJsonOnj.getJSONArray("citiesTo");
            JSONArray activeArray;

            //Начальная или конечная станция
            Intent intent = getIntent();
            boolean from = intent.getBooleanExtra(MainActivity.MESSAGE_FROM,false);
            boolean to = intent.getBooleanExtra(MainActivity.MESSAGE_TO,false);

            if(from && !to)
                activeArray = citiesFrom;
                else
                activeArray = citiesTo;

            String countryHeader="";
            String cityHeader="";

            for (int i=0; i < activeArray.length(); i++)
            {
                JSONObject object = activeArray.getJSONObject(i); //Конкретный город
                JSONArray stationsArray = object.getJSONArray("stations");  //Станции этого города
                for (int j=0; j<stationsArray.length();j++)
                {
                    JSONObject stationObject = stationsArray.getJSONObject(j);  //конкретная станция
                    //Заполнение полей station
                    Station station = new Station();
                    station.countryTitle =  stationObject.getString("countryTitle");
                    station.cityTitle =  stationObject.getString("cityTitle");

                    //Задать флаги для заголовков списка
                    if(!station.countryTitle.equals(countryHeader))
                    {
                        station.countryHeader=true;
                        countryHeader=station.countryTitle;
                    }
                    else
                        station.countryHeader=false;

                    if(!station.cityTitle.equals(cityHeader))
                    {
                        station.cityHeader=true;
                        cityHeader=station.cityTitle;
                    }
                    else
                        station.cityHeader=false;

                    station.districtTitle =  stationObject.getString("districtTitle");
                    station.regionTitle =  stationObject.getString("regionTitle");
                    station.stationTitle = stationObject.getString("stationTitle");
                    station.stationId = stationObject.getInt("stationId");
                    station.cityId = stationObject.getInt("cityId");
                    rawStations.add(station);
                    stations.add(station);
                }


            }

            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Возвращает данные о выбранной станции в MainActivity
    public void ReturnSelectedStation(View view)
    {
        ViewHolderItem tag = (ViewHolderItem)view.getTag();

        Intent intent = new Intent();
        intent.putExtra(MainActivity.MESSAGE_STATION, tag.stationName.getText().toString());
        intent.putExtra(MainActivity.MESSAGE_COUNTRY, tag.countryTitle);
        intent.putExtra(MainActivity.MESSAGE_CITY,    tag.cityTitle);
        intent.putExtra(MainActivity.MESSAGE_REGION,  tag.regionTitle);
        setResult(RESULT_OK,intent);
        finish();
    }


    //Обработчик нажатия кнопки инфо
    public void StationInfo(View view)
    {
        ViewHolderItem tag = (ViewHolderItem)view.getTag();


        Intent intent = new Intent(this, StationInfoActivity.class);

        intent.putExtra(MainActivity.MESSAGE_STATION, tag.stationName.getText().toString());
        intent.putExtra(MainActivity.MESSAGE_COUNTRY, tag.countryTitle);
        intent.putExtra(MainActivity.MESSAGE_CITY,   tag.cityTitle);
        intent.putExtra(MainActivity.MESSAGE_REGION, tag.regionTitle);


        startActivity(intent);
    }





    //Адаптер списка станций
    public class  ListAdapter extends BaseAdapter
    {


        Activity mainActivity;

        public ListAdapter(Activity main)
        {
            mainActivity=main;
        }


        @Override
        public int getCount() {
            return stations.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }




        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem holder = new ViewHolderItem();
            ImageButton infoBtn;
            RelativeLayout cellLayout;

            if(convertView == null)
            {
                LayoutInflater  inflater = (LayoutInflater)
                        mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.cell, null);



                infoBtn = (ImageButton) convertView.findViewById(R.id.infoButton);
                cellLayout = (RelativeLayout) convertView.findViewById(R.id.stationCell);
                // countrySeparator


                holder.stationName = (TextView) convertView.findViewById(R.id.stationName);
                holder.countryTitle = stations.get(position).countryTitle;
                holder.regionTitle = stations.get(position).regionTitle;
                holder.cityTitle = stations.get(position).cityTitle;
                holder.countryHeader = (TextView) convertView.findViewById(R.id.headerCountry);
                holder.cityHeader = (TextView) convertView.findViewById(R.id.headerCity);
                //Даем кнопке тэг, чтобы по нему определить кнопку в StationInfo
                infoBtn.setTag(holder);
                cellLayout.setTag(holder);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolderItem) convertView.getTag();
                holder.countryTitle = stations.get(position).countryTitle;
                holder.regionTitle = stations.get(position).regionTitle;
                holder.cityTitle = stations.get(position).cityTitle;
            }

            //Установть название станции
            holder.stationName.setText(stations.get(position).stationTitle);

            //Показываем заголовки там где показывают флаги
            if(stations.get(position).countryHeader) {
                holder.countryHeader.setVisibility(View.VISIBLE);
                holder.countryHeader.setText(stations.get(position).countryTitle);
            }
            else
                holder.countryHeader.setVisibility(View.GONE);


            if(stations.get(position).cityHeader) {
                holder.cityHeader.setVisibility(View.VISIBLE);
                holder.cityHeader.setText(stations.get(position).cityTitle);
            }
            else
                holder.cityHeader.setVisibility(View.GONE);

            return convertView;


        }
    }


    //Попытка перезагрузить файл
    public void tryReload(View view)
    {
        errorLayout.setVisibility(View.INVISIBLE);
        new Download().execute();

    }

    //Загрузка json файла из гитхаба и записать данный в MainActivity
    public class Download extends AsyncTask<Void,Void,String>
    {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        @Override
        protected String doInBackground(Void... params) {


            try
            {
                URL url = new URL("https://raw.githubusercontent.com/tutu-ru/hire_android_test/master/allStations.json");


                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine())!= null)
                {
                    buffer.append(line);
                }

                MainActivity.jsonData = buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return MainActivity.jsonData;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            getData(MainActivity.jsonData);
        }
    }



}
