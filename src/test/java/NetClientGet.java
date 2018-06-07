import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class NetClientGet {
    public static void main(String[] args) {
        new NetClientGet().getAccuweatherData("4fcee777d833a326c24f3e2f9f569f60", "37.8267,-122.4233");


    }

    public void getAccuweatherData(String apikey, String latlon) {

        try {

            URL url = new URL(("https://api.darksky.net/forecast/apiKey/latlon?exclude=currently,minutely,alerts,flags,daily&units=si").replace("apiKey", apikey).replace("latlon", latlon));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            long update_time = System.currentTimeMillis();

            String output;
            while ((output = br.readLine()) != null) {
                JSONObject json = new JSONObject(output);
                JSONObject hourly = json.getJSONObject("hourly");
                JSONArray data = hourly.getJSONArray("data");

                for (Iterator<Object> it = data.iterator(); it.hasNext(); ) {
                    JSONObject object = (JSONObject) it.next();

                    long time = object.getLong("time");
                    String summary = object.getString("summary");
                    String icon = object.getString("icon");
                    float precipIntensity = object.getFloat("precipIntensity");
                    float precipProbability = object.getFloat("precipProbability");
                    String precipType = null;
                    if (precipProbability != 0) {
                        precipType = object.getString("precipType");
                    }
                    float temperature = object.getFloat("temperature");
                    float apparentTemperature = object.getFloat("apparentTemperature");
                    float dewPoint = object.getFloat("dewPoint");
                    int humidity = (int) object.getFloat("humidity") * 100;
                    float pressure = object.getFloat("pressure");
                    float windSpeed = object.getFloat("windSpeed");
                    float windGust = object.getFloat("windGust");
                    float windBearing = object.getFloat("windBearing");
                    int cloudCover = (int) object.getFloat("cloudCover") * 100;
                    int uvIndex = object.getInt("uvIndex");
                    float visibility = object.getFloat("visibility");
                    float ozone = object.getFloat("ozone");

                    System.out.println(ozone);
                }
            }

            br.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}