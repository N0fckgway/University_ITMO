package dev.n0fckgway.lab3.beans;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ClockBean {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mm.ss");
    private static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");

    private String now;
    private String date;
    private String time;
    private LocalDateTime lastUpdate;

    public ClockBean() {
        refresh();
    }

    public void tick() {
        refresh();
    }

    private void refresh() {
        LocalDateTime current = LocalDateTime.now();
        if (lastUpdate == null || Duration.between(lastUpdate, current).getSeconds() >= 1) {
            lastUpdate = current;
            now = current.format(FULL_FORMATTER);
            date = current.format(DATE_FORMATTER);
            time = current.format(TIME_FORMATTER);
        }
    }

    public String getNow() {
        return now;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
