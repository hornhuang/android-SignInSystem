package com.example.joker.signinsystem.forums.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.forums.adapters.ArticalAdapter;
import com.example.joker.signinsystem.utils.MyToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyArticalActivity extends AppCompatActivity {

    private List<Artical> articalList;
    private ArticalAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_artical);

        iniViews();
    }

    private void iniViews(){
        recyclerView = findViewById(R.id.artical_recycler);
        swipeRefreshLayout = findViewById(R.id.artical_swipe_layout);

        iniRecycler();
        iniSwipeReflesh();
    }

    private void iniRecycler(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        articalList = new ArrayList<>();
        adapter = new ArticalAdapter(getData());
        recyclerView.setAdapter(adapter);
    }

    private void iniSwipeReflesh(){
        swipeRefreshLayout.setProgressViewOffset(false, 200, 400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    /*
    从 Bmob 获得所有用户信息
     */
    public List<Artical> getData(){
        if (BmobUser.isLogin()) {
            BmobQuery<Artical> query = new BmobQuery<>();
            query.addWhereEqualTo("linkUser", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("linkUser");
            query.findObjects(new FindListener<Artical>() {
                        @Override
                        public void done(List<Artical> object, BmobException e) {
                            if (e == null) {
                                articalList.clear();
                                setBitmap(object, object.size());
                            } else {
                                MyToast.makeToast(MyArticalActivity.this, "失败，请检查网络" + e.getMessage());
                            }
                        }
                    });
        }
        return articalList;
    }

    private void setBitmap(final List<Artical> articleList, final int size){
        new Thread(){
            @Override
            public void run() {
                for (int i = 0 ; i < articleList.size() ; i++) {
                    Artical artical = articleList.get(i);
                    artical.setArticlePhoto(getPicture(artical.getArticalImageFile().getUrl()));
                    articalList.add(artical);
                    Message message = handler.obtainMessage();
                    message.obj = 0;
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

    public static void actionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, MyArticalActivity.class);
        activity.startActivity(intent);
    }
}
