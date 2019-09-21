package com.example.sht.homework.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.BitSet;

public class LruCacheHelper {

    private static LruCache<String, Bitmap> mLruCache;

    public static LruCache<String, Bitmap> create(int maxMemory) {
        if (mLruCache != null){
            throw new RuntimeException();
        }
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        return mLruCache;
    }

    public static void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromCache(String key) {
        return mLruCache.get(key);
    }

    public static boolean isCreated(){
        return (mLruCache != null);
    }

}
