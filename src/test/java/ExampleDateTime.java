import java.util.Calendar;

public class ExampleDateTime {
    public ExampleDateTime() {

    }

    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1528354800);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        System.out.println("seconds = " + seconds);
        System.out.println("minutes = " + minutes);
        System.out.println("hours = " + hours);
    }


}
