package api.dataapi.control;

import api.dataapi.dao.Dao;
import api.dataapi.models.AccuweatherHourly;
import api.dataapi.models.DarkskyHourly;
import api.dataapi.models.Station;

import java.sql.Timestamp;
import java.util.List;

public class ThreadGetForecast implements Runnable {

	private Station station;

	public ThreadGetForecast(Station station, String ) {
		this.station = station;
	}


	public void run() {
		Dao dao = new Dao();
		GetData getData = new GetData();
		java.util.Date date= new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());
		String darkskyApiKey = dao.getApiKey("darksky");
//		List<AccuweatherHourly> list = getData.getAccuweatherHourlyForecastFromAPI(station);
//		dao.saveAccuweatherHourlyData(list, station.getStation_code(), time.toString());
//		List<DarkskyHourly> list2 = getData.getDarkskyHourlyForecastFromAPI(station, darkskyApiKey);
//		dao.saveDarkskyHourlyData(list2, station.getStation_code(), time.toString());
	}
}
