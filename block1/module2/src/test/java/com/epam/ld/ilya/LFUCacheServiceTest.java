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

    LFUCacheService lfuCacheService = new LFUCacheService();

    @Test
    @DisplayName("Should return cached data without eviction when get method called")
    void shouldReturnCachedDataWithoutEviction_whenGetMethodCalled() {
        lfuCacheService.put("key1", "value1");
        lfuCacheService.put("key2", "value2");
        lfuCacheService.put("key3", "value3");

        assertAll(
                () -> assertEquals("value1", lfuCacheService.get("key1")),
                () -> assertEquals("value2", lfuCacheService.get("key2")),
                () -> assertEquals("value3", lfuCacheService.get("key3"))
        );
    }

    @Test
    @DisplayName("Should return cached data with eviction when get method called")
    void shouldReturnCachedDataWithEviction_whenGetMethodCalled() throws InterruptedException {
        lfuCacheService.put("key1", "value1");
        lfuCacheService.put("key", "value");
        TimeUnit.SECONDS.sleep(3);
        lfuCacheService.put("key2", "value2");
        lfuCacheService.get("key");
        TimeUnit.SECONDS.sleep(3);
        lfuCacheService.put("key3", "value3");

        assertAll(
                () -> assertNull(lfuCacheService.get("key1")),
                () -> assertEquals("value",lfuCacheService.get("key")),
                () -> assertEquals("value2", lfuCacheService.get("key2")),
                () -> assertEquals("value3", lfuCacheService.get("key3"))
        );
    }

    @Test
    @DisplayName("Should remove least frequently used element when cache full")
    void shouldRemoveLFUElement_whenGetMethodCalled() {
        LFUCacheService lfuCacheService = new LFUCacheService(3);

        lfuCacheService.put("key1","value1");
        lfuCacheService.put("key2","value2");
        lfuCacheService.get("key1");
        lfuCacheService.put("key3","value3");
        lfuCacheService.put("key4","value4");

        Set<String> expected = new LinkedHashSet<>();
        expected.add("key1");
        expected.add("key3");
        expected.add("key4");

        assertEquals(expected, lfuCacheService.getCacheMap().keySet());
    }

    @Test
    @DisplayName("should get cache evictions number when get cache eviction number method called")
    void shouldGetCacheEvictionsNumber_whenGetCacheEvictionNumberCalled() {
        LFUCacheService lfuCacheService = new LFUCacheService(3);

        lfuCacheService.put("key1","value1");
        lfuCacheService.put("key2","value2");
        lfuCacheService.put("key3","value3");
        lfuCacheService.put("key4","value4");

        assertEquals(1, lfuCacheService.getCacheEvictionsNumber());
    }
}