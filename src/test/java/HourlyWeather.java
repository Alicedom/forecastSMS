import sms.SMSRule;

/*
    Class for weather data per hour
    Some function for convert data unit for many data source
 */
public class HourlyWeather {
    private String time;
    private String station_code;
    private float temperature;
    private float rain;
    private int probability;
    private int uv_index;
    private float win_speed;
    private float humidity;
    private int wind_edge;
    private String wind_direct;


    public HourlyWeather() {

    }

    public HourlyWeather(String time, String station_code, float temperature, float rain, int percent_rain, int uvIndex, float winSpeed, float humidity, int windEdge) {
        this.time = time;
        this.station_code = station_code;
        this.temperature = temperature;
        this.rain = rain;
        this.probability = percent_rain;
        this.uv_index = uvIndex;
        this.win_speed = winSpeed;
        this.humidity = humidity;
        this.wind_edge = windEdge;
        this.wind_direct = SMSRule.smsGenerateElement(this.wind_edge, SMSRule.getGetWindDirectSMSRule());
    }

    public int getHour(){
        return Integer.valueOf(time.split(" ")[1].split(":")[0]);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setTemperature(String temperature, String unit) throws NumberFormatException {
        this.temperature = Float.valueOf(temperature.replace(unit, ""));
    }

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }

    public void setRain(String rain, String unit) throws NumberFormatException{
        this.rain = Float.valueOf(rain.replace(unit, ""));
    }

    public int getUv_index() {
        return uv_index;
    }

    public void setUv_index(int uv_index) {
        this.uv_index = uv_index;
    }

    public float getWin_speed() {
        return win_speed;
    }

    public void setWin_speed(float win_speed) {
        this.win_speed = win_speed;
    }

    public void setWin_speed(String winSpeed, String unit) throws NumberFormatException{
        if (unit.contains("m/s")) {
            this.win_speed = Float.valueOf(winSpeed.replace(unit, ""));
        } else if (unit.contains("km/h")) {
            this.win_speed = 3.6f * Float.valueOf(winSpeed.replace(unit, ""));
        }
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public int getWind_edge() {
        return wind_edge;
    }

    public void setWind_edge(String wind_edge) throws NumberFormatException{
        this.wind_edge = Integer.valueOf(wind_edge.replaceAll("[^0-9]", ""));
        this.wind_direct = SMSRule.smsGenerateElement(this.wind_edge, SMSRule.getGetWindDirectSMSRule());
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public void setWindEdge(int windEdge) {
        this.wind_edge = windEdge;
    }

    public String getWind_direct() {
        return wind_direct;
    }

    public void setWind_direct(String wind_direct) {
        this.wind_direct = wind_direct;
    }
}
