package getapi.models;

public class Station {
	private String station_code;
	private String station_name;
	private float lat;
	private float lon;
	private String accuweather_key;

	public Station(String station_code, String station_name, float lat, float lon, String accuweather_key) {
		this.station_code = station_code;
		this.station_name = station_name;
		this.lat = lat;
		this.lon = lon;
		this.accuweather_key = accuweather_key;
	}

	@Override
	public String toString() {
		return "Station{" +
				"station_code='" + station_code + '\'' +
				", station_name='" + station_name + '\'' +
				", lat=" + lat +
				", lon=" + lon +
				", accuweather_key='" + accuweather_key + '\'' +
				'}';
	}

	public String getStation_code() {
		return station_code;
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getAccuweather_key() {
		return accuweather_key;
	}

	public void setAccuweather_key(String accuweather_key) {
		this.accuweather_key = accuweather_key;
	}

}
