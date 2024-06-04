package mortum;

import com.google.gson.Gson;
import mortum.dto.Ticket;
import mortum.dto.Tickets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Gson gson = new Gson();
        Tickets tickets = null;
        try {
            tickets = gson.fromJson(new FileReader("tickets.json"), Tickets.class);
        } catch (FileNotFoundException e) {
            System.out.println("Файл tickets.json должен лежать в одной директории с программой");
            System.exit(1);
        }

        List<Ticket> ticketsList = tickets.getTickets();

        task1(ticketsList);

        task2(ticketsList);

    }

    private static void task1(List<Ticket> ticketsList) {
        // цепочка стримов: отбираем билеты из Владивостока в Тель-Авив -> группируем по перевозчику ->
        // -> для каждого перевозчика ищем минимальное время полета
        ticketsList.stream().filter(i -> i.getOrigin_name().equals("Владивосток") &&
                                          i.getDestination_name().equals("Тель-Авив"))
                .collect(Collectors.groupingBy(Ticket::getCarrier))
                .forEach((carrier, tickets) -> {
                    Ticket minTimeTicket = tickets.stream().min(Ticket::compareTo).get();

                    long time1 = getTime1(minTimeTicket);
                    long totalMinutes = time1 / 1000 / 60;
                    String hours = String.valueOf(totalMinutes / 60);
                    String minutes = String.valueOf(totalMinutes % 60);
                    System.out.println("Минимальное время полета для " + carrier +
                            ": " + hours + " часов и " + minutes + " минут");
                });
        System.out.println();
    }

    private static long getTime1(Ticket minTimeTicket) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyHH:mm");
        Date depDate1 = null;
        Date arrivalDate1 = null;
        try {
            depDate1 = formatter.parse(minTimeTicket.getDeparture_date() + minTimeTicket.getDeparture_time());
            arrivalDate1 = formatter.parse(minTimeTicket.getArrival_date() + minTimeTicket.getArrival_time());
        } catch (ParseException e) {
            System.out.println("Ошибка во время парсинга даты в json");
            System.exit(1);
        }

        return arrivalDate1.getTime() - depDate1.getTime();
    }

    private static void task2(List<Ticket> ticketsList) {
        List<Integer> prices = ticketsList.stream()
                .filter(i -> i.getOrigin_name().equals("Владивосток") && i.getDestination_name().equals("Тель-Авив"))
                .map(Ticket::getPrice).toList();

        float avgPrice = ((float) prices.stream()
                .mapToInt(Integer::intValue).sum()) / prices.size();

        float med;
        List<Integer> sortedPrices = prices.stream().sorted().toList();
        if (prices.size() % 2 == 1) {
            med = sortedPrices.get(prices.size() / 2);
        } else {
            float firstValue = sortedPrices.get(prices.size() / 2 - 1);
            float secondValue = sortedPrices.get(prices.size() / 2);
            med = (firstValue + secondValue) / 2;
        }


        float diff = avgPrice - med;

        System.out.println("Медиана: " + med);
        System.out.println("Средняя цена: " + avgPrice);
        System.out.println("Разница: " + diff);
    }
}
