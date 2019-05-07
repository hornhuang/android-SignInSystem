package com.example.joker.signinsystem.bmobmanager.pictures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.print.PrinterId;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.joker.signinsystem.Summary.SummaryRecyclerAdapter;
import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.forums.adapters.ArticalAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SuperImagesLoader {

    private ArticalAdapter articalAdapter;
    private List<Artical> articalList;

    private SummaryRecyclerAdapter summaryRecyclerAdapter;
    private List<User> userList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0){
                articalAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }else if (msg.what == 020){
                summaryRecyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    public SuperImagesLoader(ArticalAdapter adapter, List<Artical> articals, SwipeRefreshLayout swipeRefreshLayout){// size 只用于区分 重载
        this.articalList = articals;
        this.articalAdapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public SuperImagesLoader(SummaryRecyclerAdapter adapter, List<User> users, SwipeRefreshLayout swipeRefreshLayout){
        this.userList = users;
        this.summaryRecyclerAdapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }


    public void articalLoad(){
        new Thread(){
            @Override
            public void run() {
                for (int i = 0 ; i < articalList.size() ; i++) {
                    if (articalList.get(i).getArticalImageFile() != null){
                        articalList.get(i).setArticlePhoto(getPicture(articalList.get(i).getArticalImageFile().getUrl()));
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
            }
        }.start();
    }

    public void userLoad(){
        new Thread(){
            @Override
            public void run() {
                for (int i = 0 ; i < userList.size() ; i++) {
                    if (userList.get(i).getImageFile() != null){
                        userList.get(i).setHeadIcon(getPicture(userList.get(i).getImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 020;
                    handler.sendMessage(message);
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
