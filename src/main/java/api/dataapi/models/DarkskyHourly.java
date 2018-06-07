package api.dataapi.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DarkskyHourly {
	private String time;
	private String summary;
	private String icon;
	private float precipIntensity;
	private float precipProbability;
	private String precipType;
	private float temperature;
	private float apparentTemperature;
	private float dewPoint;
	private float humidity;
	private float pressure;
	private float windSpeed;
	private float windGust;
	private float windBearing;
	private float cloudCover;
	private float uvIndex;
	private float ozone;
	private float visibility;

	public String getTime() {
		String str = time +"000";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(Long.parseLong(str));
		String time_ = format.format(date);
		return time_;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public float getPrecipIntensity() {
		return precipIntensity;
	}

	public void setPrecipIntensity(float precipIntensity) {
		this.precipIntensity = precipIntensity;
	}

	public int getPrecipProbability() {
		return (int) ( precipProbability*100);
	}

	public void setPrecipProbability(float precipProbability) {
		this.precipProbability = precipProbability;
	}

	public String getPrecipType() {
		return precipType;
	}

	public void setPrecipType(String precipType) {
		this.precipType = precipType;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public Float getApparentTemperature() {
		return apparentTemperature;
	}

	public void setApparentTemperature(float apparentTemperature) {
		this.apparentTemperature = apparentTemperature;
	}

	public float getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}

	public Integer getHumidity() {
		return (int) (humidity*100);
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public float getWindGust() {
		return windGust;
	}

	public void setWindGust(float windGust) {
		this.windGust = windGust;
	}

	public float getWindBearing() {
		return windBearing;
	}

	public void setWindBearing(float windBearing) {
		this.windBearing = windBearing;
	}

	public int getCloudCover() {
		return  (int) (100*cloudCover);
	}

	public void setCloudCover(float cloudCover) {
		this.cloudCover = cloudCover;
	}

	public float getUvIndex() {
		return uvIndex;
	}

	public void setUvIndex(float uvIndex) {
		this.uvIndex = uvIndex;
	}

	public float getOzone() {
		return ozone;
	}

	public void setOzone(float ozone) {
		this.ozone = ozone;
	}

	public float getVisibility() {
		return visibility;
	}

	public void setVisibility(float visibility) {
		this.visibility = visibility;
	}
}
