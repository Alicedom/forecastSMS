package getapi.control;

import getapi.dao.Dao;
import getapi.models.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        Dao dao = new Dao();
        ArrayList<Station> listStation = dao.getStationEnalbeApi();
        int numThread = listStation.size();// kich thuoc cua thread pool
        dao.close();

        try {
            ScheduledExecutorService darkService = Executors.newScheduledThreadPool(numThread);

            Thread saveForecast = new Thread(new ThreadGetForecast(listStation, "darksky"));
//            saveForecast.start();
            darkService.scheduleWithFixedDelay(saveForecast, 0, 1, TimeUnit.HOURS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        try {
            ScheduledExecutorService accuService = Executors.newScheduledThreadPool(1);
            Thread saveForecast = new Thread(new ThreadGetForecast(listStation, "accuweather"));
//            saveForecast.start();
            accuService.scheduleWithFixedDelay(saveForecast, 0, 1, TimeUnit.HOURS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }


    }
}
