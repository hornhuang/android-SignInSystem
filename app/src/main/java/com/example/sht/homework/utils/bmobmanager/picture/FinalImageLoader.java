package com.example.sht.homework.utils.bmobmanager.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

public final class FinalImageLoader {

    private final String TAG = "FinalImageLoader";

    private ImageView imageView;
    private CircleImageView circleImageView;

    private BmobFile bmobFile;

    private Bitmap bitmap;
    private int flag;// 用于匹配构造方法

    private RecyclerView.Adapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                imageView.setImageBitmap(bitmap);
            }else if (msg.what == 1){
                circleImageView.setImageBitmap(bitmap);
            }else if (msg.what == 2){
                imageView.setImageBitmap(bitmap);
                adapter.notifyDataSetChanged();
            }else if (msg.what == 3){
                circleImageView.setImageBitmap(bitmap);
                adapter.notifyDataSetChanged();
            }
        }
    };

    public FinalImageLoader(ImageView imageView, BmobFile bmobFile){
        this.imageView = imageView;
        this.bmobFile = bmobFile;
        this.flag = 0;
    }

    public FinalImageLoader(CircleImageView imageView, BmobFile bmobFile){
        this.circleImageView = imageView;
        this.bmobFile = bmobFile;
        this.flag = 1;
    }

    public FinalImageLoader(RecyclerView.Adapter adapter, ImageView imageView, BmobFile bmobFile){
        this.imageView = imageView;
        this.bmobFile = bmobFile;
        this.adapter = adapter;
        this.flag = 2;
    }

    public FinalImageLoader(RecyclerView.Adapter adapter, CircleImageView imageView, BmobFile bmobFile){
        this.circleImageView = imageView;
        this.bmobFile = bmobFile;
        this.adapter = adapter;
        this.flag = 3;
    }

    public void loadSmall(){
        new Thread(){
            @Override
            public void run() {
                bitmap = getPicture(bmobFile.getUrl());
            }
        }.start();
    }

    public void loadHuge(){
        new Thread(){
            @Override
            public void run() {
                bitmap = getPicture(bmobFile.getUrl());
            }
        }.start();
    }

    public Bitmap getPicture(String path){
        Bitmap bm = null;
        try{
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            if (bm != null){
                bitmap = Bitmap.createScaledBitmap(bm, 100, 100, false);
                Message message = handler.obtainMessage();
                message.what = flag;
                handler.sendMessage(message);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "MalformedURLException" + e.getMessage() + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException" + e.getMessage() + e.toString());
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Exception" + e.getMessage() + e.toString());
        }
        return bm;
    }
}
