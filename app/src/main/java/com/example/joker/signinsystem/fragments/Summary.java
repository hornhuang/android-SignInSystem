package com.example.joker.signinsystem.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.Summary.SummaryRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 排行榜
 * 读取所有用户信息在 RecyclerView 中显示
 * 用户通过点击每个 item 可以查看该用户细节柱状图
 */
public class Summary extends Fragment{

    private List<User> userList = new ArrayList<>();
    public static String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";
    private RecyclerView mUserListViews;
    private SwipeRefreshLayout mRefreshLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if((Integer)msg.obj==0){
                mUserListViews.setAdapter(new SummaryRecyclerAdapter(userList));
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_summary , container, false);
        //第二：默认初始化
        Bmob.initialize(getActivity(),APPID);

        // 下拉刷新
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setProgressViewOffset(false, 200, 400);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListViews.setAdapter(new SummaryRecyclerAdapter(userList));
                chnage();
            }
        });

        // 初始化加载信息
        mUserListViews = (RecyclerView) view.findViewById(R.id.listview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mUserListViews.setLayoutManager(manager);
        chnage();

        return view;
    }

    /*
    定义一个Handler，定义延时执行的行为
    */
    public  void chnage(){
        new Thread(){
            @Override
            public void run() {
                getData();
                while ( userList.size() == 0 ){

                }
                Message message = handler.obtainMessage();
               message.obj = 0;
               handler.sendMessage(message);
            }
        }.start();
    }

    /*
    从 Bmob 获得所有用户信息
     */
    public List<User> getData(){
        userList = new ArrayList<>();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    userList = list;
                }
                else {
                    Toast.makeText(getActivity(),e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        return userList;
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        }else{  // 在最前端显示 相当于调用了onResume();
            //网络数据刷新
        }
    }

}
