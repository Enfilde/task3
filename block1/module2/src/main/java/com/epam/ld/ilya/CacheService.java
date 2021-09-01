package com.epam.ld.ilya;

public interface CacheService {

    void put(String key, String data);

    String get(String key);
}
