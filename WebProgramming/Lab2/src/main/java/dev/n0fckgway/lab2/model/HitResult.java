package dev.n0fckgway.lab2.model;

import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.SimpleFormatter;


@Getter
public class HitResult {
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;
    private final String nowTime;
    private final long execTime;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public HitResult(double x, double y, double r, boolean hit, LocalDateTime nowTime, long execTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.nowTime = nowTime.format(FORMATTER);
        this.execTime = execTime;
    }


}
