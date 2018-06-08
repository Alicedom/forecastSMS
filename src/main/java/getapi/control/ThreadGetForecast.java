package getapi.control;

import getapi.dao.Dao;
import getapi.models.AccuweatherHourly;
import getapi.models.DarkskyHourly;
import getapi.models.Station;

import java.sql.Timestamp;
import java.util.List;

public class ThreadGetForecast implements Runnable {

	private Station station;
	private String website;

	public ThreadGetForecast(Station station , String website) {
		this.station = station;
		this.website = website;
	}


	public void run() {
		Dao dao = new Dao();
		GetData getData = new GetData();


		String accuweatherApiKey = dao.getApiKey(website);
		List list = getData.getHourlyForecastFromAPI(station, accuweatherApiKey,website);
		dao.saveHourlyData(list, station.getStation_code(), website);

	}
}
