package com.example.sht.homework.utils.bmobmanager.pictures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.sht.homework.summary.SummaryRecyclerAdapter;
import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.forums.adapters.ArticalAdapter;

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
        for (int i = 0 ; i < articalList.size() ; i++) {
            final int flag = i;
            new Thread(){
                @Override
                public void run() {
                    Artical artical = articalList.get(flag);
                    if (artical.getArticalImageFile() != null){
                        artical.setArticlePhoto(getPicture(artical.getArticalImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 0x0;
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    public void userLoad(){
        for (int i = 0 ; i < userList.size() ; i++) {
            final int flag = i;
            new Thread(){
                @Override
                public void run() {
                    User user = userList.get(flag);
                    if (user.getImageFile() != null){
                        user.setHeadIcon(getPicture(user.getImageFile().getUrl()));
                    }
                    Message message = handler.obtainMessage();
                    message.what = 020;
                    handler.sendMessage(message);
                }
            }.start();
        }
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
