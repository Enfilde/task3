package com.epam.ld.ilya;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LFUCacheServiceTest {

    private static final String KEY = "key";
    private static final String KEY_1 = "key1";
    private static final String KEY_2 = "key2";
    private static final String KEY_3 = "key3";
    private static final String KEY_4 = "key4";
    private static final String VALUE = "value";
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_3 = "value3";
    private static final String VALUE_4 = "value4";

    LFUCacheService lfuCacheService = new LFUCacheService();

    @Test
    @DisplayName("Should return cached data without eviction when get method called")
    void shouldReturnCachedDataWithoutEviction_whenGetMethodCalled() {
        lfuCacheService.put(KEY_1, VALUE_1);
        lfuCacheService.put(KEY_2, VALUE_2);
        lfuCacheService.put(KEY_3, VALUE_3);

        assertAll(
                () -> assertEquals(VALUE_1, lfuCacheService.get(KEY_1)),
                () -> assertEquals(VALUE_2, lfuCacheService.get(KEY_2)),
                () -> assertEquals(VALUE_3, lfuCacheService.get(KEY_3))
        );
    }

    @Test
    @DisplayName("Should return cached data with eviction when get method called")
    void shouldReturnCachedDataWithEviction_whenGetMethodCalled() throws InterruptedException {
        lfuCacheService.put(KEY_1, VALUE_1);
        lfuCacheService.put(KEY, VALUE);
        TimeUnit.SECONDS.sleep(3);
        lfuCacheService.put(KEY_2, VALUE_2);
        lfuCacheService.get(KEY);
        TimeUnit.SECONDS.sleep(3);
        lfuCacheService.put(KEY_3, VALUE_3);

        assertAll(
                () -> assertNull(lfuCacheService.get(KEY_1)),
                () -> assertEquals(VALUE, lfuCacheService.get(KEY)),
                () -> assertEquals(VALUE_2, lfuCacheService.get(KEY_2)),
                () -> assertEquals(VALUE_3, lfuCacheService.get(KEY_3))
        );
    }

    @Test
    @DisplayName("Should remove least frequently used element when cache full")
    void shouldRemoveLFUElement_whenGetMethodCalled() {
        LFUCacheService lfuCacheService = new LFUCacheService(3);

        lfuCacheService.put(KEY_1, VALUE_1);
        lfuCacheService.put(KEY_2, VALUE_2);
        lfuCacheService.get(KEY_1);
        lfuCacheService.put(KEY_3, VALUE_3);
        lfuCacheService.put(KEY_4, VALUE_4);

        Set<String> expected = new LinkedHashSet<>();
        expected.add(KEY_1);
        expected.add(KEY_3);
        expected.add(KEY_4);

        assertEquals(expected, lfuCacheService.getCacheMap().keySet());
    }

    @Test
    @DisplayName("should get cache evictions number when get cache eviction number method called")
    void shouldGetCacheEvictionsNumber_whenGetCacheEvictionNumberCalled() {
        LFUCacheService lfuCacheService = new LFUCacheService(3);

        lfuCacheService.put(KEY_1, VALUE_1);
        lfuCacheService.put(KEY_2, VALUE_2);
        lfuCacheService.put(KEY_3, VALUE_3);
        lfuCacheService.put(KEY_4, VALUE_4);

        assertEquals(1, lfuCacheService.getCacheEvictionsNumber());
    }
}