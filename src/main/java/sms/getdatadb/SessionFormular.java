package sms.getdatadb;

import java.util.*;

/*
    calculate sum, max, min, average data from List of Weather forecast Data
 */
public class SessionFormular {

    private  SMSRule rule = new SMSRule();
    private  List<HourlyWeather> list;

    private float maxTemperature, minTemperature;
    private int averageHumidity;
    private String maxWindDirect;
    private float averageWindSpeed ;
    private int numberOfSun;

    private Hashtable<String,Float> rain;
    private Hashtable<String,Integer> percentRain;

    public SessionFormular(List<HourlyWeather> list) {
        this.list = list;
        calculateAverageHumidity();
        calculateAverageWindSpeed();
        calculateMinMaxTemperatue();
        calculateRainAndPercentRain();
        if(list.get(0).getWind_direct().isEmpty()){
            maxWindDirect = "It have not Wind Direct";
        }else{
            calculateMaxWindDirect();
        }
    }

    private void calculateMaxWindDirect(){
        Hashtable<String,Integer> listWindDirect = new Hashtable<String, Integer>();
        for (HourlyWeather data:list){
            String direct = data.getWind_direct();
            if(listWindDirect.containsKey(direct)){
//                int count = listWindDirect.get(direct)+1;
//                listWindDirect.remove(direct);
//                listWindDirect.put(direct,count);
                listWindDirect.replace(direct,listWindDirect.get(direct)+1);
            }else
                listWindDirect.put(direct,1);
        }

        int numberDirectMax=0;

        for(Map.Entry<String,Integer> entry : listWindDirect.entrySet()) {
            if(entry.getValue() >= numberDirectMax) {
                numberDirectMax = entry.getValue();
                maxWindDirect = entry.getKey();
            }
        }
    }

    private void calculateAverageWindSpeed(){
        averageWindSpeed =0;
        float totalWindSpeed=0;

        for (HourlyWeather data:list){
            totalWindSpeed += data.getWin_speed();
        }
        if (totalWindSpeed != 0){
            this.averageWindSpeed =  totalWindSpeed/list.size();
        }
    }

    private void calculateAverageHumidity(){
        averageHumidity =0;
        int totalHumidity =0;

        for (HourlyWeather data:list){
            totalHumidity += data.getHumidity();
        }
        if (totalHumidity != 0){
            this.averageHumidity = totalHumidity/list.size();
        }
    }

    private void calculateMinMaxTemperatue(){
        maxTemperature = Float.MIN_VALUE;
        minTemperature = Float.MAX_VALUE;

        for (HourlyWeather data:list){
            float temperature = data.getTemperature();
            if(temperature > maxTemperature) maxTemperature = temperature;
            if(temperature < minTemperature) minTemperature = temperature;
        }
    }

    private void calculateRainAndPercentRain(){

        rain = new Hashtable<String,Float>();
        percentRain = new Hashtable<String, Integer>();

        for (HourlyWeather data:list){
            String session = getSession(data);
            if(rain.containsKey(session) ){
//                float sumRain = rain.get(session)+data.getRain();
//                rain.remove(session);
//                rain.put(session, sumRain );

                rain.replace(session,rain.get(session)+data.getRain());
                if(percentRain.get(session) < data.getProbability() ){
//                    percentRain.remove(session);
//                    percentRain.put(session,data.getProbability());
                    percentRain.replace(session,data.getProbability());
                }

            }else{
                rain.put(session,data.getRain());
                percentRain.put(session,data.getProbability());

            }
        }

        Enumeration<String> iter = percentRain.keys();

        while (iter.hasMoreElements()) {
            String key = iter.nextElement();

            if (rain.get(key) ==0 && percentRain.get(key) < 20){
                rain.remove(key);
                percentRain.remove(key);
            }
        }

    }

    private String getSession(HourlyWeather data) {
        return rule.getSession(data.getHour());
    }


    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public int getAverageHumidity(){
        return averageHumidity;
    }

    public String getMaxWindDirect(){
        return maxWindDirect;
    }

    public String getRainAndPercentRain(){
        StringBuilder rainSMS= new StringBuilder();

        Set<String> keys = rain.keySet();
        for (String key: keys) {
            rainSMS.append("\n").append("Buổi ").append(key).append(" có mưa ").append(rain.get(key)).append("mm với xác suất ").append(percentRain.get(key));
        }

        return rainSMS.toString();
    }

    public Hashtable<String, Float> getRain() {
        return rain;
    }

    public Hashtable<String, Integer> getPercentRain() {
        return percentRain;
    }

}
