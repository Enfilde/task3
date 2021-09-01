package com.epam.ld.ilya;

import com.epam.ld.ilya.entity.Cache;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LFUCacheService implements CacheService {

    private static final Logger LOGGER = Logger.getLogger(LFUCacheService.class.getName());

    private static final String REMOVED_FROM_CACHE_MESSAGE = "REMOVED FROM CACHE : ";
    private static final int DEFAULT_MAXIMUM_CACHE_SIZE = 100_000;
    private static final int CACHE_LIFETIME = 5;

    private final Map<String, Cache> cacheMap;
    private final Map<String, LocalTime> cacheTimeMap;
    private final ConcurrentSkipListMap<LocalTime, LinkedHashSet<String>> sameTimeCacheMap;
    private final ScheduledExecutorService service;

    private final int maximumSize;
    private int cacheEvictionsNumber;

    /**
     * LFU cache service constructor
     */
    public LFUCacheService() {
        this.maximumSize = DEFAULT_MAXIMUM_CACHE_SIZE;
        cacheMap = new ConcurrentHashMap<>();
        cacheTimeMap = new ConcurrentHashMap<>();
        sameTimeCacheMap = new ConcurrentSkipListMap<>();
        service = Executors.newSingleThreadScheduledExecutor();
        cleanExpiredCache(); // clean expired cache
    }

    /**
     * LFU cache service constructor
     * @param maximumSize cache volume
     */
    public LFUCacheService(int maximumSize) {
        this.maximumSize = maximumSize;
        cacheMap = new ConcurrentHashMap<>();
        cacheTimeMap = new ConcurrentHashMap<>();
        sameTimeCacheMap = new ConcurrentSkipListMap<>();
        service = Executors.newSingleThreadScheduledExecutor();
        cleanExpiredCache();
    }

    @Override
    public void put(String key, String data) {
        var cache = new Cache(data);
        LocalTime timeFromLastAccess = cache.getTimeFromLastAccess();

        if (cacheMap.size() >= maximumSize) {
            String first = sameTimeCacheMap.firstEntry().getValue().stream().findFirst().get();
            var removedCache = cacheMap.remove(first);
            cacheEvictionsNumber += 1;
            LOGGER.info(REMOVED_FROM_CACHE_MESSAGE + removedCache);
            sameTimeCacheMap.firstEntry().getValue().remove(first);
            cacheTimeMap.remove(first);
        }

        cacheMap.put(key, cache);
        cacheTimeMap.put(key, cache.getTimeFromLastAccess());

        if (sameTimeCacheMap.containsKey(timeFromLastAccess)) {
            sameTimeCacheMap.get(timeFromLastAccess).add(key);
        } else {
            LinkedHashSet<String> keys = new LinkedHashSet<>();
            keys.add(key);
            sameTimeCacheMap.put(timeFromLastAccess, keys);
        }
    }

    @Override
    public String get(String key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        sameTimeCacheMap.get(cacheTimeMap.get(key)).remove(key);
        String data = cacheMap.get(key).getData();
        var cache = new Cache(data);
        cacheMap.put(key, new Cache(data));
        cacheTimeMap.put(key, cache.getTimeFromLastAccess());
        return data;
    }

    public int getCacheEvictionsNumber() {
        return cacheEvictionsNumber;
    }

    public void shutdownCleaner() {
        service.shutdownNow();
    }

    protected Map<String, Cache> getCacheMap() {
        return cacheMap;
    }

    private void cleanExpiredCache() {
        Runnable thread = () -> {
            Set<String> expired = getExpiredCacheKeys();
            cacheEvictionsNumber += expired.size();
            expired.forEach(cache -> {
                cacheMap.remove(cache);
                LOGGER.info(REMOVED_FROM_CACHE_MESSAGE + cache);
            });
        };

        service.scheduleAtFixedRate(thread, 0, 1, TimeUnit.SECONDS);
    }

    private Set<String> getExpiredCacheKeys() {
        return cacheMap.entrySet()
                .stream()
                .filter(cache -> cache.getValue().getTimeFromLastAccess().plusSeconds(CACHE_LIFETIME).isBefore(LocalTime.now()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
