package com.example.joker.signinsystem.forums.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.forums.adapters.ArticalAdapter;
import com.example.joker.signinsystem.utils.Toasty;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Artical> articalList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton mWriteButton;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArticalAdapter adapter = new ArticalAdapter(articalList);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            Toasty.Toasty(ForumActivity.this, "更新成功" + articalList.size());
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        iniViews();

    }

    private void iniViews(){
        recyclerView = findViewById(R.id.artical_recycler);
        swipeRefreshLayout = findViewById(R.id.artical_swipe_layout);
        mWriteButton = findViewById(R.id.write);

        swipeRefreshLayout.setProgressViewOffset(false, 200, 400);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });

        mWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditArticalActivity.actionStart(ForumActivity.this);
            }
        });

        ArticalAdapter adapter = new ArticalAdapter(getData());
        recyclerView.setAdapter(adapter);
    }

    /*
    从 Bmob 获得所有用户信息
     */
    public List<Artical> getData(){
        articalList = new ArrayList<>();
        BmobQuery<Artical> query = new BmobQuery<>();
        query.setLimit(8).setSkip(1).order("-createdAt")
                .findObjects(new FindListener<Artical>() {
                    @Override
                    public void done(List<Artical> object, BmobException e) {
                        if (e == null) {
                            articalList = object;
                            Message message = handler.obtainMessage();
                            message.obj = 0;
                            handler.sendMessage(message);
                        } else {
                            Toasty.Toasty(ForumActivity.this, "失败，请检查网络" + e.getMessage());
                        }
                    }
                });
        return articalList;
    }

    public static void actionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, ForumActivity.class);
        activity.startActivity(intent);
    }

}
