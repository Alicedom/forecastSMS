package sms;

import java.util.*;

/*
    calculate sum, max, min, average data from List of Weather forecast Data
 */
public class SessionFormular {

    private List<HourlyWeather> list;

    private float maxTemperature, minTemperature;
    private int averageHumidity;
    private String maxWindDirect;
    private float averageWindSpeed;
    private int numberMaxSun;
    private float totalUV;

    private Hashtable<String, Float> rain;
    private Hashtable<String, Integer> percentRain;

    public SessionFormular(List<HourlyWeather> list) {
        this.list = list;

        maxTemperature = Float.MIN_VALUE;
        minTemperature = Float.MAX_VALUE;
        averageHumidity = 0;
        averageWindSpeed = 0.0f;
        numberMaxSun =0;
        totalUV = 0;

        rain = new Hashtable<String, Float>();
        percentRain = new Hashtable<String, Integer>();


        if(! list.isEmpty()){
            calculateAverageHumidity();
            calculateAverageWindSpeed();
            calculateMinMaxTemperatue();
            calculateRainAndPercentRain();
            calculateMaxWindDirect();
            calculateAvgUVAndNumberMaxSun();
        }

    }

    private void calculateMaxWindDirect() {
        Hashtable<String, Integer> listWindDirect = new Hashtable<String, Integer>();

        for (HourlyWeather data : list) {
            String direct = data.getWind_direct();

            if (listWindDirect.containsKey(direct)) {
                listWindDirect.replace(direct, listWindDirect.get(direct) + 1);
            } else
                listWindDirect.put(direct, 1);
        }

        int numberDirectMax = 0;

        for (Map.Entry<String, Integer> entry : listWindDirect.entrySet()) {
            if (entry.getValue() >= numberDirectMax) {
                numberDirectMax = entry.getValue();
                maxWindDirect = entry.getKey();
            }
        }
    }

    private void calculateAverageWindSpeed() {

        float totalWindSpeed = 0;

        for (HourlyWeather data : list) {
            totalWindSpeed += data.getWin_speed();
        }
        if (totalWindSpeed != 0) {
            this.averageWindSpeed = totalWindSpeed / list.size();
        }
    }

    private void calculateAverageHumidity() {

        int totalHumidity = 0;

        for (HourlyWeather data : list) {
            totalHumidity += data.getHumidity();
        }
        if (totalHumidity != 0) {
            this.averageHumidity = totalHumidity / list.size();
        }
    }

    private void calculateMinMaxTemperatue() {

        for (HourlyWeather data : list) {
            float temperature = data.getTemperature();
            if (temperature > maxTemperature) maxTemperature = temperature;
            if (temperature < minTemperature) minTemperature = temperature;
        }
    }

    private void calculateRainAndPercentRain() {

        for (HourlyWeather data : list) {
            String session = getSession(data);

            if (rain.containsKey(session)) {

                rain.replace(session, rain.get(session) + data.getRain());
                if (percentRain.get(session) < data.getProbability()) {
                    percentRain.replace(session, data.getProbability());
                }

            } else {
                rain.put(session, data.getRain());
                percentRain.put(session, data.getProbability());
            }
        }

        Enumeration<String> iter = percentRain.keys();

        while (iter.hasMoreElements()) {
            String key = iter.nextElement();

            if (rain.get(key) == 0 && percentRain.get(key) < 20) {
                rain.remove(key);
                percentRain.remove(key);
            }
        }

    }

    private void calculateNumberSum(){
        Hashtable<String, Integer> listSun = new Hashtable<String, Integer>();

        for (HourlyWeather data : list) {
            String typeOfSun = getTypeOfSun(data.getUv_index());

            if (listSun.containsKey(typeOfSun)) {
                listSun.replace(typeOfSun, listSun.get(typeOfSun) + 1);
            } else
                listSun.put(typeOfSun, 1);
        }

        System.out.println("listSun.toString() = " + listSun.toString());
    }

    private void calculateAvgUVAndNumberMaxSun(){

        int uvRuleSize = SMSRule.getUvSMSRule().length;
        String maxSun = (String) SMSRule.getUvSMSRule()[uvRuleSize-1][2];
        System.out.println("maxSun = " + maxSun);


        for (HourlyWeather data : list){
            totalUV +=data.getUv_index();

            if(maxSun.equals(getTypeOfSun(data.getUv_index()))){
                numberMaxSun ++;
            }
        }

    }

    private String getSession(HourlyWeather data) {
        return SMSRule.getSession(data.getHour());
    }

    private String getTypeOfSun(float uvData){
        return SMSRule.smsGenerateElement(uvData, SMSRule.getUvSMSRule());
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public String getReport_MaxTemperature(){
        String report =null;

        if(maxTemperature == Float.MIN_VALUE){
            report = "Không có nhiệt độ tối đa";
        }else{
            report = "Nhiệt độ tối đa: " + maxTemperature;
        }

        return report;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public String getReport_MinTemperature(){
        String report =null;

        if(minTemperature == Float.MAX_VALUE){
            report = "Không có nhiệt độ tối thiếu";
        }else{
            report = "Nhiệt độ tối thiểu: " + minTemperature;
        }

        return report;
    }

    public int getAverageHumidity() {
        return averageHumidity;
    }

    public String getReport_AverageHumidity(){
        String report =null;

        if(averageHumidity == 0){
            report = "Không có độ ẩm trung bình";
        }else{
            report = "Độ ẩm trung bình: " + averageHumidity;
        }

        return report;
    }

    public float getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public String getReport_AverageWindSpeed(){
        String report =null;

        if(averageWindSpeed ==0){
            report = "Không có tốc độ gió trung bình";
        }else{
            report = "Tốc độ gió trung bình: " + averageWindSpeed;
        }

        return report;
    }

    public String getMaxWindDirect() {
        return maxWindDirect;
    }

    public String getReport_MaxWindDirect() {
        String report =null;

        if(maxWindDirect == null){
            report = "Không có hướng gió thường xuyên nhất";
        }else{
            report = "Hướng gió nhiều nhất: " + maxWindDirect;;
        }

        return report;
    }

    public Hashtable<String, Float> getRain() {
        return rain;
    }

    public Hashtable<String, Integer> getPercentRain() {
        return percentRain;
    }

    public String getReport_RainAndPercentRain() {
        StringBuilder rainSMS = new StringBuilder();

        if (rain.isEmpty()) {
            rainSMS.append("Trời không mưa trong cả ngày");
        } else {
            Set<String> keys = rain.keySet();

            rainSMS.append( "Dự báo buổi mưa:");
            for (String key : keys) {
                rainSMS.append("\n").append("Buổi ").append(key).append(" có mưa ").append(rain.get(key)).append("mm với xác suất ").append(percentRain.get(key));
            }
        }
        return rainSMS.toString();
    }

    public int getNumberMaxSun() {
        return numberMaxSun;
    }

    public String getReport_NumberMaxSun(){
        String report =null;

        if(numberMaxSun == 0){
            report = "Không có nắng";
        }else{
            report = "Giờ nắng trong ngày: " + numberMaxSun;;
        }

        return report;

    }

    public float getTotalUV() {
        return totalUV;
    }

    public String getReport_AverageSun(){
        String report =null;

        if(totalUV == 0){
            report = "Không có nắng";
        }else{
            report = "Trung bình bức xạ trong ngày: " + totalUV;;
        }

        return report;

    }
}
