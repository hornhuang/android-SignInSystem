package com.example.sht.homework.forums.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.bmobmanager.pictures.SuperImagesLoader;
import com.example.sht.homework.forums.adapters.ArticalAdapter;
import com.example.sht.homework.utils.MyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyForumActivity extends BaseActivity {

    private Toolbar toolbar;

    private List<Artical> articalList;
    private ArticalAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_artical);

        iniViews();
        iniRecycler();
        iniSwipeReflesh();
        iniToolbar();
    }

    private void iniViews(){
        recyclerView = findViewById(R.id.artical_recycler);
        swipeRefreshLayout = findViewById(R.id.artical_swipe_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void iniToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
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
                                articalList.addAll(object);
                                new SuperImagesLoader(adapter, articalList, swipeRefreshLayout).articalLoad();
//                                setBitmap(object);
                            } else {
                                MyToast.makeToast(MyForumActivity.this, "失败，请检查网络" + e.getMessage());
                            }
                        }
                    });
        }else {
            MyToast.makeToast(MyForumActivity.this, "请先登录");
        }
        return articalList;
    }

    public static void actionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, MyForumActivity.class);
        activity.startActivity(intent);
    }
}
