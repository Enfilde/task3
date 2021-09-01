package com.epam.ld.ilya;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LRUCacheServiceTest {

    private static final String KEY_A = "A";
    private static final String KEY_B = "B";
    private static final String KEY_C = "C";
    private static final String KEY_D = "D";
    private static final String VALUE_A = "aaa";
    private static final String VALUE_B = "bbb";
    private static final String VALUE_C = "ccc";
    private static final String VALUE_D = "ddd";

    @Test
    @DisplayName("Should get cache key when get cache method called")
    void shouldGetCacheKey_whenGetCacheKeyMethodCalled() throws ExecutionException {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.put(KEY_A, VALUE_A);
        lruCache.put(KEY_B, VALUE_B);
        lruCache.put(KEY_C, VALUE_C);

        assertEquals(VALUE_A, lruCache.get(KEY_A));
    }

    @Test
    @DisplayName("Should get nothing when cache absent")
    void shouldGetNothing_whenCacheAbsent() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.put(KEY_A, VALUE_A);
        lruCache.put(KEY_B, VALUE_B);
        lruCache.put(KEY_C, VALUE_C);


        assertNull(lruCache.getIfPresent("V"));
    }

    @Test
    @DisplayName("Should correct put in cache")
    void shouldCorrectPutInCache_whenPutMethodCalled() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.put(KEY_A, VALUE_A);
        lruCache.put(KEY_B, VALUE_B);
        lruCache.put(KEY_C, VALUE_C);
        lruCache.put(KEY_D, VALUE_D);

        String result = lruCache.getIfPresent(KEY_A);

        assertEquals(1, lruCache.getNumberOfCacheEvictions());
        assertNull(result);
        assertAll(
                () -> assertEquals(1, lruCache.getNumberOfCacheEvictions()),
                () -> assertNull(result)
        );
    }

    @Test
    @DisplayName("should cache logic work correct")
    void shouldCacheLogicWorkCorrect() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.put(KEY_A, VALUE_A);
        lruCache.put(KEY_B, VALUE_B);
        lruCache.getIfPresent(KEY_A);
        lruCache.put(KEY_C, VALUE_C);
        lruCache.getIfPresent(KEY_B);
        lruCache.put(KEY_D, VALUE_D);
        lruCache.put(KEY_A, VALUE_A);

        String deletedEntity = lruCache.getIfPresent(KEY_C);
        lruCache.getAverageTimeOfEvictions();

        assertAll(
                () -> assertEquals(VALUE_A, lruCache.getIfPresent(KEY_A)),
                () -> assertEquals(VALUE_D, lruCache.getIfPresent(KEY_D)),
                () -> assertEquals(VALUE_B, lruCache.getIfPresent(KEY_B)),
                () -> assertNull(deletedEntity)
        );
    }
}