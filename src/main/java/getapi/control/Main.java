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
        System.out.println("Number active station: " + numThread);


        try{
            ScheduledExecutorService darkService = Executors.newScheduledThreadPool(numThread);
            for (Station station : listStation) {
                Thread saveForecast = new Thread(new ThreadGetForecast(station, "darksky"));
//            saveForecast.start();
                darkService.scheduleWithFixedDelay(saveForecast, 0, 15, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }


        //accuweather chi lay 1 vi key it
        try {
            ScheduledExecutorService accuService = Executors.newScheduledThreadPool(1);
            Station station = listStation.get(0);
            Thread saveForecast = new Thread(new ThreadGetForecast(station, "accuweather"));
//            saveForecast.start();
            accuService.scheduleWithFixedDelay(saveForecast, 0, 30, TimeUnit.MINUTES);

        }catch (Exception e){
            logger.error(e.getMessage());
        }


    }
}
