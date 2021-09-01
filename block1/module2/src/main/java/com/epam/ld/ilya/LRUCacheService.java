package com.epam.ld.ilya;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LRUCacheService implements CacheService {

    private static final Logger LOGGER = Logger.getLogger(LRUCacheService.class.getName());

    private static final String REMOVED_MESSAGE = "was removed";
    private static final String TIME_OF_REMOVAL_MESSAGE = "Time of removal: ";
    private static final String NUMBER_OF_CACHE_EVICTIONS = "Current number of cache evictions: ";
    private static final String AVERAGE_TIME_MESSAGE = "Average time is: ";

    private final int capacity;

    private LoadingCache<String, String> loadingCache;
    private RemovalListener<String, String> removalListener;
    private int numberOfCacheEvictions;
    private long wholeTimeOfEvictions;


    public LRUCacheService(int capacity) {
        this.capacity = capacity;
        this.numberOfCacheEvictions = 0;
    }

    public int getNumberOfCacheEvictions() {
        return numberOfCacheEvictions;
    }

    /**
     * set removal listener
     */
    public void setRemovalListener() {
        long startTime = System.currentTimeMillis();
        removalListener = notification -> {
            numberOfCacheEvictions++;
            LOGGER.info(notification.getKey() + REMOVED_MESSAGE);
            LOGGER.info(NUMBER_OF_CACHE_EVICTIONS + numberOfCacheEvictions);
        };
        long totalTime = System.currentTimeMillis() - startTime;
        LOGGER.info(TIME_OF_REMOVAL_MESSAGE + totalTime);
        wholeTimeOfEvictions += totalTime; // whole time of cache evictions
    }

    /**
     * init loading cache
     */
    public void initLoadingCache() {
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String key) {
                return key;
            }
        };
        loadingCache = CacheBuilder
                .newBuilder()
                .maximumSize(capacity)
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build(cacheLoader);
    }

    @Override
    public void put(String key, String data) {
        loadingCache.put(key, data);
    }

    @Override
    public String get(String key) {
        try {
            return loadingCache.get(key);
        } catch (ExecutionException e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    public String getIfPresent(String key) {
        return loadingCache.getIfPresent(key);
    }

    public void getAverageTimeOfEvictions() {
        double time = (double) wholeTimeOfEvictions / (double) numberOfCacheEvictions;
        LOGGER.info(AVERAGE_TIME_MESSAGE + time);
    }
}
