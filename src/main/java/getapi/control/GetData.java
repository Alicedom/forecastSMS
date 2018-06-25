package getapi.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import getapi.models.AccuweatherHourly;
import getapi.models.DarkskyHourly;
import getapi.models.Station;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetData {
    private final static Logger logger = LoggerFactory.getLogger(GetData.class);

    // doget
    public String doGet(String url) {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();
        org.apache.http.HttpResponse response = null;

        String data = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toString(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return data;
    }

    public List getHourlyForecastFromAPI(Station station, String apiKey, String website) {
        List listData = null;
        if (website.equals("accuweather")) {
            listData = getAccuweatherHourlyForecastFromAPI(station, apiKey);
        } else if (website.equals("darksky")) {
            listData = getDarkskyHourlyForecastFromAPI(station, apiKey);
        }
        return listData;
    }

    // get darksky hourly forecast from api
    private List<DarkskyHourly> getDarkskyHourlyForecastFromAPI(Station station, String darkskyKey) {
        List<DarkskyHourly> listForecast = null;

        String url = "https://api.darksky.net/forecast/darkkey/latlon?units=si";
        url = url.replace("darkkey", darkskyKey).replace("latlon", station.getLat() + "," + station.getLon());

        String data1 = doGet(url);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(data1);
        String data = jsonObject.get("hourly").toString();

       
//        logger.info("data hourly: " + data);
        Gson gson = new Gson();
        jsonObject = (JsonObject) jsonParser.parse(data);
        TypeToken<List<DarkskyHourly>> token = new TypeToken<List<DarkskyHourly>>() {
        };
        listForecast = gson.fromJson(jsonObject.get("data").toString(), token.getType());
        return listForecast;
    }

    // get accuweather hourly forcast form api
    private List<AccuweatherHourly> getAccuweatherHourlyForecastFromAPI(Station station, String accuweatherKey) {
        String URL = "http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/locationKey?apikey=accuweatherKey&details=true&metric=true";

        URL = URL.replace("locationKey", station.getAccuweather_key()).replace("accuweatherKey", accuweatherKey);
        String json = doGet(URL);

        Gson gson = new Gson();
        TypeToken<List<AccuweatherHourly>> token = new TypeToken<List<AccuweatherHourly>>() {
        };
        List<AccuweatherHourly> listForecast = gson.fromJson(json, token.getType());
        return listForecast;
    }

    // lay cac locationkey theo lat,lon
    public String requestGetAccuweatherLocationKey(String latlon, String accuweatherKey) {
        String URL = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=" + accuweatherKey + "&q=" + latlon;
        String json = doGet(URL);
        // lay key
        String locationKey = new JSONObject(json).getString("Key");

        return locationKey;
    }

}
