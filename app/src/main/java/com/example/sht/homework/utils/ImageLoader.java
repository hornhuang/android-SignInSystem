package com.example.sht.homework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import io.reactivex.annotations.NonNull;

public class ImageLoader {

    /**
     * 获得适合土坯啊显示的 Options
     *
     * @param path 图片路径
     * @return
     */
    public static BitmapFactory.Options getCompatibleOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return options;
    }

    /**
     * 计算 inSampleSize 由于图片显示的缩放
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateSampleSize(BitmapFactory.Options options,
                                          int reqWidth, int reqHeight) {
        final int width  = options.outWidth;
        final int height = options.outHeight;
        // round() 方法可把一个数字舍入为最接近的整数。
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            final int widthRatio  = Math.round(width / reqWidth);
            final int heightRatio = Math.round(height / reqHeight);
            // 获得最小缩放倍数
            inSampleSize = widthRatio < heightRatio
                    ? width : height;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromFile(String path,@NonNull ImageView imageView) throws NullPointerException{
        if (path == null) {
            throw new NullPointerException("图片路径不能为空");
        }
        BitmapFactory.Options options = getCompatibleOptions(path);
        Sise sise = getViewSize(imageView);
        options.inSampleSize = calculateSampleSize(options, sise.viewWidth, sise.viewHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 获得 ImageView 尺寸
     *
     * @param imageView
     * @return
     */
    private static Sise getViewSize(@NonNull ImageView imageView) {
        int width  = imageView.getWidth();
        int height = imageView.getHeight();
        return new Sise(width, height);
    }

    private static class Sise {
        int viewWidth;
        int viewHeight;

        Sise(int width, int height) {
            viewWidth  = width;
            viewHeight = height;
        }
    }
}
