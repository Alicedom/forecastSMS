package getapi.control;

import getapi.dao.Dao;
import getapi.models.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class ThreadGetForecast implements Runnable {

    private List<Station> stationList;
    private String website;

    public ThreadGetForecast(List<Station> stationList, String website) {
        this.stationList = stationList;
        this.website = website;
    }

    private final static Logger logger = LoggerFactory.getLogger(ThreadGetForecast.class);

    public void run() {
        try {
            java.util.Date date = new java.util.Date();
            final String time = new Timestamp(date.getTime()).toString();

            for (Station station : stationList) {
                Dao dao = new Dao();
                String apikey = dao.getApiKey(website);

                if (apikey != null || apikey.isEmpty()) {
                    GetData getData = new GetData();
                    List list = getData.getHourlyForecastFromAPI(station, apikey, website);
                    if (list != null) {
                        dao.saveHourlyData(list, station.getStation_code(), website, time);
                    }
                }

                dao.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }
}
