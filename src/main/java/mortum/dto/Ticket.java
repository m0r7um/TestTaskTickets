package mortum.dto;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class Ticket implements Comparable<Ticket>{
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private String departure_date;
    private String departure_time;
    private String arrival_date;
    private String arrival_time;
    private String carrier;
    private int stops;
    private int price;

    @Override
    public int compareTo(Ticket t) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyHH:mm");
        try {
            Date depDate1 = formatter.parse(this.getDeparture_date() + this.getDeparture_time());
            Date arrivalDate1 = formatter.parse(this.getArrival_date() + this.getArrival_time());

            long time1 = arrivalDate1.getTime() - depDate1.getTime();

            Date depDate2 = formatter.parse(t.getDeparture_date() + t.getDeparture_time());
            Date arrivalDate2 = formatter.parse(t.getArrival_date() + t.getArrival_time());

            long time2 = arrivalDate2.getTime() - depDate2.getTime();

            return (int) (time1 - time2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
