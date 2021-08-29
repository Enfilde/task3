package com.epam.ld.ilya;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Cache {

    private final String data;

    private final LocalTime timeFromLastAccess;

    Cache(String data) {
        this.data = data;
        timeFromLastAccess = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public String getData() {
        return data;
    }

    public LocalTime getTimeFromLastAccess() {
        return timeFromLastAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cache cache = (Cache) o;
        return Objects.equals(data, cache.data) && Objects.equals(timeFromLastAccess, cache.timeFromLastAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, timeFromLastAccess);
    }

    @Override
    public String toString() {
        return "Cache{" +
                "data='" + data + '\'' +
                ", timeFromLastAccess=" + timeFromLastAccess +
                '}';
    }
}

