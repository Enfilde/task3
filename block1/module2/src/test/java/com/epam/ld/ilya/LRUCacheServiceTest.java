package com.epam.ld.ilya;

import com.epam.ld.ilya.entity.CustomEntity;
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
    private static final CustomEntity ENTITY_A = new CustomEntity("aaa");
    private static final CustomEntity ENTITY_B = new CustomEntity("bbb");
    private static final CustomEntity ENTITY_C = new CustomEntity("ccc");
    private static final CustomEntity ENTITY_D = new CustomEntity("ddd");

    @Test
    @DisplayName("Should get cache key when get cache method called")
    void shouldGetCacheKey_whenGetCacheKeyMethodCalled() throws ExecutionException {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.putLoadingCache(KEY_A, ENTITY_A);
        lruCache.putLoadingCache(KEY_B, ENTITY_B);
        lruCache.putLoadingCache(KEY_C, ENTITY_C);

        CustomEntity customEntity = lruCache.getCacheKeyLoadingCache(KEY_A);

        assertEquals(ENTITY_A, customEntity);
    }

    @Test
    @DisplayName("Should get nothing when cache absent")
    void shouldGetNothing_whenCacheAbsent() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.putLoadingCache(KEY_A, ENTITY_A);
        lruCache.putLoadingCache(KEY_B, ENTITY_B);
        lruCache.putLoadingCache(KEY_C, ENTITY_C);

        CustomEntity customEntity = lruCache.getIfPresentLoadingCache("V");

        assertNull(customEntity);
    }

    @Test
    @DisplayName("Should correct put in cache")
    void shouldCorrectPutInCache_whenPutMethodCalled() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.putLoadingCache(KEY_A, ENTITY_A);
        lruCache.putLoadingCache(KEY_B, ENTITY_B);
        lruCache.putLoadingCache(KEY_C, ENTITY_C);
        lruCache.putLoadingCache(KEY_D, ENTITY_D);

        CustomEntity customEntity = lruCache.getIfPresentLoadingCache(KEY_A);

        assertEquals(1, lruCache.getNumberOfCacheEvictions());
        assertNull(customEntity);
        assertAll(
                () -> assertEquals(1, lruCache.getNumberOfCacheEvictions()),
                () -> assertNull(customEntity)
        );
    }

    @Test
    @DisplayName("should cache logic work correct")
    void shouldCacheLogicWorkCorrect() {
        LRUCacheService lruCache = new LRUCacheService(3);
        lruCache.setRemovalListener();
        lruCache.initLoadingCache();
        lruCache.putLoadingCache(KEY_A, ENTITY_A);
        lruCache.putLoadingCache(KEY_B, ENTITY_B);
        lruCache.getIfPresentLoadingCache(KEY_A);
        lruCache.putLoadingCache(KEY_C, ENTITY_C);
        lruCache.getIfPresentLoadingCache(KEY_B);
        lruCache.putLoadingCache(KEY_D, ENTITY_D);
        lruCache.putLoadingCache(KEY_A, ENTITY_A);

        CustomEntity deletedEntity = lruCache.getIfPresentLoadingCache(KEY_C);
        lruCache.getAverageTimeOfEvictions();

        assertAll(
                () -> assertEquals(ENTITY_A, lruCache.getIfPresentLoadingCache(KEY_A)),
                () -> assertEquals(ENTITY_D, lruCache.getIfPresentLoadingCache(KEY_D)),
                () -> assertEquals(ENTITY_B, lruCache.getIfPresentLoadingCache(KEY_B)),
                () -> assertNull(deletedEntity)
        );
    }
}