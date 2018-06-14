package getapi.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccuweatherHourly {
    private int EpochDateTime;
    private String IconPhrase;
    private boolean IsDaylight;
    private Sensor Temperature;
    private Sensor RealFeelTemperature;
    private Sensor WetBulbTemperature;
    private Sensor DewPoint;
    private Wind Wind;
    private WindGust WindGust;
    private int RelativeHumidity;
    private Sensor Visibility;
    private Sensor Ceiling;
    private int UVIndex;
    private int PrecipitationProbability;
    private int RainProbability;
    private int SnowProbability;
    private int IceProbability;
    private Sensor TotalLiquid;
    private Sensor Rain;
    private Sensor Snow;
    private Sensor Ice;
    private int CloudCover;

    public String getIconPhrase() {
        return IconPhrase;
    }

    public void setIconPhrase(String iconPhrase) {
        IconPhrase = iconPhrase;
    }

    public int isIsDaylight() {
        if (IsDaylight == true) {
            return 1;
        } else return 0;
    }

    public void setIsDaylight(boolean isDaylight) {
        IsDaylight = isDaylight;
    }

    public Sensor getSnow() {
        return Snow;
    }

    public void setSnow(Sensor snow) {
        Snow = snow;
    }

    public Sensor getTemperature() {
        return Temperature;
    }

    public void setTemperature(Sensor temperature) {
        Temperature = temperature;
    }

    public Sensor getRealFeelTemperature() {
        return RealFeelTemperature;
    }

    public void setRealFeelTemperature(Sensor realFeelTemperature) {
        RealFeelTemperature = realFeelTemperature;
    }

    public Sensor getWetBulbTemperature() {
        return WetBulbTemperature;
    }

    public void setWetBulbTemperature(Sensor wetBulbTemperature) {
        WetBulbTemperature = wetBulbTemperature;
    }

    public Sensor getDewPoint() {
        return DewPoint;
    }

    public void setDewPoint(Sensor dewPoint) {
        DewPoint = dewPoint;
    }

    public Wind getWind() {
        return Wind;
    }

    public void setWind(Wind wind) {
        Wind = wind;
    }

    public WindGust getWindGust() {
        return WindGust;
    }

    public void setWindGust(WindGust windGust) {
        WindGust = windGust;
    }

    public int getRelativeHumidity() {
        return RelativeHumidity;
    }

    public void setRelativeHumidity(int relativeHumidity) {
        RelativeHumidity = relativeHumidity;
    }

    public Sensor getVisibility() {
        return Visibility;
    }

    public void setVisibility(Sensor visibility) {
        Visibility = visibility;
    }

    public Sensor getCeiling() {
        return Ceiling;
    }

    public void setCeiling(Sensor ceiling) {
        Ceiling = ceiling;
    }

    public int getUVIndex() {
        return UVIndex;
    }

    public void setUVIndex(int uVIndex) {
        UVIndex = uVIndex;
    }

    public int getPrecipitationProbability() {
        return PrecipitationProbability;
    }

    public void setPrecipitationProbability(int precipitationProbability) {
        PrecipitationProbability = precipitationProbability;
    }

    public int getRainProbability() {
        return RainProbability;
    }

    public void setRainProbability(int rainProbability) {
        RainProbability = rainProbability;
    }

    public int getSnowProbability() {
        return SnowProbability;
    }

    public void setSnowProbability(int snowProbability) {
        SnowProbability = snowProbability;
    }

    public int getIceProbability() {
        return IceProbability;
    }

    public void setIceProbability(int iceProbability) {
        IceProbability = iceProbability;
    }

    public Sensor getTotalLiquid() {
        return TotalLiquid;
    }

    public void setTotalLiquid(Sensor totalLiquid) {
        TotalLiquid = totalLiquid;
    }

    public Sensor getRain() {
        return Rain;
    }

    public void setRain(Sensor rain) {
        Rain = rain;
    }

    public Sensor getIce() {
        return Ice;
    }

    public void setIce(Sensor ice) {
        Ice = ice;
    }

    public int getCloudCover() {
        return CloudCover;
    }

    public void setCloudCover(int cloudCover) {
        CloudCover = cloudCover;
    }

    public String getTime() {
        String str = this.EpochDateTime + "000";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(str));
        return format.format(date);
    }

    public class WindGust {
        private Sensor Speed;

        public Sensor getSpeed() {
            return Speed;
        }

        public void setSpeed(Sensor speed) {
            Speed = speed;
        }

    }

    public class Wind {
        private Sensor Speed;
        private Direction Direction;

        public Sensor getSpeed() {
            return Speed;
        }

        public void setSpeed(Sensor speed) {
            Speed = speed;
        }

        public Direction getDirection() {
            return Direction;
        }

        public void setDirection(Direction direction) {
            Direction = direction;
        }

        public class Direction {
            private int Degrees;
            private String Localized;

            public int getDegrees() {
                return Degrees;
            }

            public void setDegrees(int degrees) {
                Degrees = degrees;
            }

            public String getLocalized() {
                return Localized;
            }

            public void setLocalized(String localized) {
                Localized = localized;
            }

            @Override
            public String toString() {
                return Degrees + " " + Localized;
            }

        }

    }

    public class Sensor {
        private Float Value;
        private String Unit;

        public Float getValue() {
            if (Unit.equals("km/h")) {
                Value = Float.valueOf(Value / 3.6f);
                Unit = "m/s";
            }
            return Value;
        }

        public void setValue(Float value) {
            Value = value;

        }

        public String getUnit() {

            return Unit;
        }

        public void setUnit(String unit) {
            Unit = unit;
        }

        @Override
        public String toString() {
            return Value + " " + Unit;
        }

    }
}
