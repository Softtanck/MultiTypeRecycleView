package com.cheweishi.android.utils;

import android.support.v4.util.LruCache;

/**
 * 缓存内存 - Utils
 *
 * @author wangxy
 * @version 1.0
 */
public class LruCacheUtils {

    /**
     * 缓存json数据
     */
    private static LruCache<String, String> mJsonCache = new LruCache<String, String>(512);

    /**
     * 不可实例化
     */
    private LruCacheUtils() {

    }


    /**
     * 添加进入缓存列表
     *
     * @param key
     * @param value
     */
    public static void addJsonLruCache(String key, String value) {
        mJsonCache.put(key, value);
    }

    /**
     * 从缓存列表中拿出来
     *
     * @param key
     * @return
     */
    public static String getJsonLruCache(String key) {
        return mJsonCache.get(key);
    }

    /**
     * 清理缓存列表
     */
    public static void removeJsonLruCache(String key) {
        mJsonCache.remove(key);
    }

    /**
     * 取代
     */
    public static void replaceJsonLruCache(String key, String value) {
        if (null == value)
            return;
        if (mJsonCache.get(key) != null) {
            mJsonCache.remove(key);
        }
        mJsonCache.put(key, value);
    }

    /**
     * 清空所有
     */
    public static void removeAll() {
        mJsonCache.evictAll();
    }


}
