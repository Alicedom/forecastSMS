package getapi.control;

import getapi.dao.Dao;
import getapi.models.Station;

import java.util.List;

public class ThreadGetForecast implements Runnable {

    private Station station;
    private String website;

    public ThreadGetForecast(Station station, String website) {
        this.station = station;
        this.website = website;
    }


    public void run() {
        Dao dao = new Dao();
        String apikey = dao.getApiKey(website);

        if (apikey != null || apikey.isEmpty()) {
            GetData getData = new GetData();
            List list = getData.getHourlyForecastFromAPI(station, apikey, website);
            dao.saveHourlyData(list, station.getStation_code(), website);
        }


    }
}
