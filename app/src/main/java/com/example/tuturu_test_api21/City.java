package com.example.tuturu_test_api21;


import java.util.ArrayList;

public class City{
    String countryTitle;
    Point point;
    String districtTitle;
    int cityId;
    String cityTitle;
    ArrayList<Station> stations;

    public City()
    {
         countryTitle="Test";
         point = new Point();
         districtTitle="Test";
         cityId = 404;
         cityTitle = "Test";
    }


}
