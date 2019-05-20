package com.example.sht.homework.utils.bmobmanager.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader extends Handler {
    private ImageView mImageView;
    private String mUrl;
    private Bitmap mBitmap;
    private ViewTreeObserver vto;

    public ImageLoader(ImageView imageView, String url) {
        mImageView = imageView;
        mUrl = url;
    }

    public ImageLoader(ImageView imageView) {
        mImageView = imageView;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try{
                        URL url=new URL(mUrl);
                        URLConnection connection=url.openConnection();
                        connection.connect();
                        InputStream inputStream=connection.getInputStream();
                        mBitmap= BitmapFactory.decodeStream(inputStream);
                        sendEmptyMessage(1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }else {
            if(mImageView.getMeasuredWidth()==0) {
                vto = mImageView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        vto.removeOnGlobalLayoutListener(this);
                        adjustViewHeight();
                    }
                });
            }else{
                adjustViewHeight();
            }
        }
    }

    public void load(){
        sendEmptyMessage(0);
    }

    private void adjustViewHeight(){
        mImageView.setImageBitmap(mBitmap);
    }

}
