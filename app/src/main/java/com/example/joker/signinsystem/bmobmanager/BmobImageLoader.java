package com.example.joker.signinsystem.bmobmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BmobImageLoader extends Handler {

    private ImageView imageView;
    private String uri;

    private Bitmap bitmap;

    public BmobImageLoader(ImageView imageView, final String uri){
        this.imageView = imageView;
        this.uri = uri;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        imageView.setImageBitmap(bitmap);
    }

    public void loadImage(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                getPicture();
            }
        };
    }

    public void getPicture(){
        try{
            URL url=new URL(uri);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            Message message = new Message();
            message.what = 0x0;
            handleMessage(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
