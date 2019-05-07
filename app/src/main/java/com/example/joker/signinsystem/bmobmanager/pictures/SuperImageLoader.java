package com.example.joker.signinsystem.bmobmanager.pictures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.baseclasses.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuperImageLoader {

    private ImageView imageView;
    private CircleImageView circleImageView;

    private User user;
    private Artical artical;

    private Bitmap bitmap;
    private int flag;// 用于匹配构造方法

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                imageView.setImageBitmap(user.getHeadIcon());
            }else if (msg.what == 1){
                circleImageView.setImageBitmap(user.getHeadIcon());
            }else if (msg.what == 2){
                imageView.setImageBitmap(artical.getArticlePhoto());
            }else if (msg.what == 3){
                circleImageView.setImageBitmap(artical.getArticlePhoto());
            }
        }
    };

    public SuperImageLoader(ImageView imageView, User user){
        this.imageView = imageView;
        this.user = user;
        this.flag = 0;
    }

    public SuperImageLoader(CircleImageView imageView, User user){
        this.circleImageView = imageView;
        this.user = user;
        this.flag = 1;
    }

    public SuperImageLoader(ImageView imageView, Artical artical){
        this.imageView = imageView;
        this.artical = artical;;
        this.flag = 2;
    }

    public SuperImageLoader(CircleImageView imageView, Artical artical){
        this.circleImageView = imageView;
        this.artical = artical;
        this.flag = 3;
    }

    public void articalLoad(){
        new Thread(){
            @Override
            public void run() {
                    if (artical.getArticalImageFile() != null){
                        artical.setArticlePhoto(getPicture(artical.getArticalImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 0x0;
                    handler.sendMessage(message);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }.start();
    }

    public void userLoad(){
        new Thread(){
            @Override
            public void run() {
                    if (user.getImageFile() != null){
                        user.setHeadIcon(getPicture(user.getImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = flag;
                    handler.sendMessage(message);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }
}
