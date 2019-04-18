package com.iprogrammerr.time.ruler.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SmartDate {

    private final ZonedDateTime date;

    public SmartDate(ZonedDateTime date) {
        this.date = date;
    }

    public SmartDate(long date) {
        this(ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC));
    }

    public long ofYearMonthSeconds(int year, int month) {
        return ofYearMonth(year, month).toEpochSecond();
    }

    public ZonedDateTime ofYearMonth(int year, int month) {
        return ZonedDateTime.of(
            year, month, date.getDayOfMonth(),
            date.getHour(), date.getMinute(), date.getSecond(), 0, date.getZone()
        );
    }

    public long ofTime(int hour, int minute, int second) {
        return ZonedDateTime.of(
            date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
            hour, minute, second, 0, date.getZone()
        ).toEpochSecond();
    }

    public long dayBeginning() {
        return ofTime(0, 0, 0);
    }

    public long dayEnd() {
        return ofTime(23, 59, 59);
    }
}
