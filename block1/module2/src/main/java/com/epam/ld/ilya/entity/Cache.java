package com.epam.ld.ilya.entity;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Cache {

    private final String data;

    private final LocalTime timeFromLastAccess;

    public Cache(String data) {
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
    public boolean equals(Object element) {
        if (this == element) {
            return true;
        }
        if (element == null || getClass() != element.getClass()) {
            return false;
        }
        Cache cache = (Cache) element;
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

