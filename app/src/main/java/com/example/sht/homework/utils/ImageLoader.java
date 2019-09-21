package com.example.sht.homework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import io.reactivex.annotations.NonNull;

public class ImageLoader {
    // 图片默认压缩为该大小
    private final static int VIEW_SIZE = 100;

    /**
     * 获得适合土坯啊显示的 Options
     *
     * @param path 图片路径
     * @return
     */
    public static <T> BitmapFactory.Options getCompatibleOptions(T path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (path.getClass().equals(String.class)){
            String s = path.toString();
            BitmapFactory.decodeFile(s, options);
        } else if (path.getClass().equals(Integer.class)){
            int a = Integer.valueOf(path.toString());
            BitmapFactory.decodeResource(AppContext.getInstance().getApplicationContext().getResources(), a, options);
        }

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
        if (reqHeight == 0 || reqWidth == 0){
            reqHeight = reqWidth = VIEW_SIZE;
        }
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

    public static <T> Bitmap decodeSampleBitmap(@NonNull T path,@NonNull ImageView imageView) throws NullPointerException{
        BitmapFactory.Options options = getCompatibleOptions(path);
        Sise sise = new Sise(imageView.getWidth(), imageView.getHeight());
        options.inSampleSize = calculateSampleSize(options, sise.viewWidth, sise.viewHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap;
        if (path.getClass().equals(String.class)) {
            bitmap = BitmapFactory.decodeFile(path.toString(), options);
        }else if (path.getClass().equals(Integer.class)) {
            bitmap = BitmapFactory.decodeResource(AppContext.getInstance().getApplicationContext().getResources(),
                    Integer.valueOf(path.toString()));
        }else {
            throw new NullPointerException("path 参数必须为 Integer 或 String 类型");
        }
        return bitmap;
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
        if (width > 1 || height > 1) {
            width = height = VIEW_SIZE;
        }
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

    public static void loadBitmapByResId(Integer resId, ImageView imageView) {
        String key = resId.toString();
        Bitmap bitmap = LruCacheHelper.getBitmapFromCache(key);
        if (bitmap == null) {
            BitmapTask task = new BitmapTask(imageView);
            task.execute(resId);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    static class BitmapTask extends AsyncTask<Integer, Void, Bitmap> {

        private ImageView imageView;

        public BitmapTask(ImageView img) {
            this.imageView = img;
        }

        @Override
        protected Bitmap doInBackground(Integer... classes) {
            Bitmap bitmap = decodeSampleBitmap(classes[0],
                    imageView);
            LruCacheHelper.addBitmapToCache(String.valueOf(classes[0]), bitmap);
            return bitmap;
        }
    }
}
