package com.example.joker.signinsystem.MainFragment;

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


public class Summary extends Fragment{

    private List<User> userList = new ArrayList<>();
    private int n ;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_summary , container, false);
//        thread.start();
        //第二：默认初始化
        Bmob.initialize(getActivity(),APPID);

        // 下拉刷新
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<User> list=getData();
                mUserListViews.setAdapter(new SummaryRecyclerAdapter(userList));
                chnage();
            }
        });

        // 初始化加载信息
        mUserListViews = (RecyclerView) view.findViewById(R.id.listview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mUserListViews.setLayoutManager(manager);
//        mRefreshLayout.setRefreshing(true);
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



    public List<User> getData(){
        userList = new ArrayList<>();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    n = list.size();
                    System.out.println(n);
                    for (int i = 0; i < n; i++) {
                        userList.add(list.get(i)) ;
                    }
                }
                else {
                    Toast.makeText(getActivity(),e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }

        });

//        for (int i = 0; i < n; i++) {
//            User user = new HashMap<String, Object>();
////            map.put("image", R.drawable.thumb);
//            map.put("title", userList.get(i).getUsername());
//            map.put("info", userList.get(i) .getMobilePhoneNumber());
//            userList.add(map);
//        }
        return userList;
    }


}
