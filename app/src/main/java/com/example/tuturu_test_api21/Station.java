package com.example.tuturu_test_api21;

/**
 * Created by Александр on 18.10.2016.
 */

public class Station implements Cloneable {
    String countryTitle;
    Point point;

    String districtTitle;
    int cityId;
    String cityTitle;
    String regionTitle;

    int stationId;
    String stationTitle;

    boolean countryHeader;  //Флаги отвечаеют за отображение заголвка в списке
    boolean cityHeader;

    @Override
    protected Object clone() throws CloneNotSupportedException {

        return super.clone();
    }
}
