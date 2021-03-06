package sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/*
    calculate sum, max, min, average data from List of Weather forecast Data
 */
public class SessionFormular {

    private final static Logger logger = LoggerFactory.getLogger(SessionFormular.class);
    private List<HourlyWeather> list;
    private double maxTemperature, minTemperature;
    private double averageHumidity;
    private String maxWindDirect;
    private double averageWindSpeed;
    private int numberMaxSun;
    private double totalUV;
    private Hashtable<String, Double> rain;
    private Hashtable<String, Double> percentRain;

    public SessionFormular(List<HourlyWeather> list) {
        this.list = list;

        maxTemperature = Double.MIN_VALUE;
        minTemperature = Double.MAX_VALUE;
        averageHumidity = 0;
        averageWindSpeed = 0.0f;
        numberMaxSun = 0;
        totalUV = 0;
        rain = new Hashtable<String, Double>();
        percentRain = new Hashtable<String, Double>();

        if (list.size() > 0) {
            if (list.size() != 24) {
                logger.info("Không đủ dữ liệu 24h");
            }
            calculateAverageHumidity();
            calculateAverageWindSpeed();
            calculateMinMaxTemperatue();
            calculateRainAndPercentRain();
            calculateMaxWindDirect();
            calculateAvgUVAndNumberMaxSun();
        } else {
            logger.info("Không có dữ liệu để tính toán");
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

        double totalWindSpeed = 0;

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
            double temperature = data.getTemperature();
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

            double rainmount = (double) (Math.round(rain.get(key) * 10.0) / 10.0);
            if (rainmount == 0 || percentRain.get(key) < 20) {
                rain.remove(key);
                percentRain.remove(key);
            }
        }

    }

    private void calculateNumberSum() {
        Hashtable<String, Integer> listSun = new Hashtable<String, Integer>();

        for (HourlyWeather data : list) {
            String typeOfSun = getTypeOfSun(data.getUv_index());

            if (listSun.containsKey(typeOfSun)) {
                listSun.replace(typeOfSun, listSun.get(typeOfSun) + 1);
            } else
                listSun.put(typeOfSun, 1);
        }

    }

    private void calculateAvgUVAndNumberMaxSun() {

        int uvRuleSize = SMSRule.getUvSMSRule().length;
        String maxSun = (String) SMSRule.getUvSMSRule()[uvRuleSize - 1][2];

        for (HourlyWeather data : list) {
            totalUV += data.getUv_index();

            if (maxSun.equals(getTypeOfSun(data.getUv_index()))) {
                numberMaxSun++;
            }
        }
    }

    private String getSession(HourlyWeather data) {
        return SMSRule.getSession(data.getHour());
    }

    private String getTypeOfSun(double uvData) {
        return SMSRule.smsGenerateElement(uvData, SMSRule.getUvSMSRule());
    }

    public String getMaxTemperature() {

        if (maxTemperature == Double.MIN_VALUE)
            return "";
        else
            return String.valueOf(String.valueOf(Math.round(maxTemperature)));
    }

    public String getReport_MaxTemperature() {
        String report = null;

        if (maxTemperature == Float.MIN_VALUE) {
            report = "Không có nhiệt độ cao nhất";
        } else {
            report = "Nhiệt độ cao nhất: " + String.valueOf(Math.round(maxTemperature)) + " độ C";
        }

        return report;
    }

    public String getMinTemperature() {
        if (minTemperature == Float.MAX_VALUE)
            return "";
        else
            return String.valueOf(Math.round(minTemperature));
    }

    public String getReport_MinTemperature() {
        String report = null;

        if (minTemperature == Float.MAX_VALUE) {
            report = "Không có nhiệt độ thấp nhất";
        } else {
            report = "Nhiệt độ thấp nhất: " + String.valueOf(Math.round(minTemperature)) + " độ C";
        }

        return report;
    }

    public double getAverageHumidity() {
        return Math.round(averageHumidity);
    }

    public String getReport_AverageHumidity() {
        String report = null;

        if (averageHumidity == 0) {
            report = "Không có độ ẩm trung bình";
        } else {
            report = "Độ ẩm trung bình: " + Math.round(averageHumidity) + "%";
        }

        return report;
    }

    public double getAverageWindSpeed() {
        return (double) (Math.round(averageWindSpeed * 10.0) / 10.0);
    }

    public String getReport_AverageWindSpeed() {
        String report = null;

        if (averageWindSpeed == 0) {
            report = "";
        } else {
            report = "cấp " + SMSRule.smsGenerateElement(averageWindSpeed, SMSRule.getWindSpeedConvertLevelRule());
        }

        return report;
    }

    public String getMaxWindDirect() {
        return maxWindDirect;
    }

    public String getReport_MaxWindDirect() {
        String report = null;

        if (maxWindDirect == null) {
            report = "";
        } else {
            report = "Gió " + maxWindDirect;
        }

        return report;
    }

    public Hashtable<String, Double> getRain() {

        // lam tron trong tinh toan
        return rain;
    }

    public Hashtable<String, Double> getPercentRain() {
        return percentRain;
    }

    public String getReport_RainAndPercentRain() {
        StringBuilder rainSMS = new StringBuilder();

        if (rain.isEmpty()) {
            rainSMS.append("Không mưa");
        } else {
            List<String> keys = SMSRule.getListSMS(SMSRule.getSession());

            rainSMS.append("Dự báo buổi mưa:");
            for (String key : keys) {
                if (rain.containsKey(key)) {
                    rainSMS.append("\n").append(key).append(SMSRule.smsGenerateElement(rain.get(key), SMSRule.getGetRainSMSRule()));
                }
            }
        }
        return rainSMS.toString();
    }

    public int getNumberMaxSun() {
        return numberMaxSun;
    }

    public String getReport_NumberMaxSun() {
        String report = null;

        if (numberMaxSun == 0) {
            report = "Không nắng";
        } else {
            report = "Giờ nắng trong ngày: " + numberMaxSun;
        }

        return report;

    }

    public double getTotalUV() {
        return totalUV;
    }

    public String getReport_TotalUV() {
        String report = null;

        if (totalUV == 0) {
            report = "Không nắng";
        } else {
            report = "Trung bình bức xạ trong ngày: " + totalUV;
        }

        return report;

    }
}
