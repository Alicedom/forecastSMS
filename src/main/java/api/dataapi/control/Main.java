package api.dataapi.control;

import api.dataapi.dao.Dao;
import api.dataapi.models.Station;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Dao dao = new Dao();

        ArrayList<Station> listStation = null;

        listStation = dao.getEnalbeApiStation();
        int numThread = listStation.size();// kich thuoc cua thread pool
        System.out.println("Number active station: " + numThread);


//			ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numThread);
			for (Station station : listStation) {
				System.out.println(station.toString());
				Thread saveForecast = new Thread( new ThreadGetForecast(station));
				saveForecast.start();
////				executorService.scheduleWithFixedDelay(saveForecast, 0, 8, TimeUnit.HOURS);
			}

    }
}
