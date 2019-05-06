package com.example.joker.signinsystem.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.Summary.SummaryRecyclerAdapter;
import com.example.joker.signinsystem.managers.ListContentMate;

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
    private RecyclerView mUserListViews;
    private SwipeRefreshLayout mRefreshLayout;
    private SearchView mSearch;
    private List<User> mUserTotalList;
    private SummaryRecyclerAdapter recyclerAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if((Integer)msg.obj==0){
                recyclerAdapter = new SummaryRecyclerAdapter(userList);
                mUserListViews.setAdapter(recyclerAdapter);
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_summary , container, false);

        iniViews(view);
        iniReFleshLayout();
        iniRecyclerView();
        iniSearch();
        getData();

        return view;
    }

    private void iniViews(View view){
        mRefreshLayout = view.findViewById(R.id.refreshLayout);// 下拉刷新
        mUserListViews = (RecyclerView) view.findViewById(R.id.listview);// 初始化加载信息
        mSearch = view.findViewById(R.id.search);
    }

    private void iniReFleshLayout(){
        mRefreshLayout.setProgressViewOffset(false, 200, 400);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserListViews.setAdapter(new SummaryRecyclerAdapter(userList));
                getData();
            }
        });
    }

    private void iniRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mUserListViews.setLayoutManager(manager);
    }

    /*
    SearchView 文字变化 动态匹配
     */
    private void iniSearch(){
        setTextColor();

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<User> mList = ListContentMate.mate(mUserTotalList, mSearch.getQuery().toString());
                userList.clear();
                userList.addAll(mList);
                recyclerAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    /*
    设置 SearchView 文字颜色
     */
    private void setTextColor(){
        EditText textView = (EditText) mSearch
                .findViewById(
                        android.support.v7.appcompat.R.id.search_src_text
                );
        textView.setTextColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.white)
        );
        textView.setHintTextColor(
                ContextCompat.getColor(
                        getContext(),
                        R.color.gray)
        );
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
                    mUserTotalList = new ArrayList<>();
                    userList = sort(list);
                    mUserTotalList.addAll(userList);
                    Message message = handler.obtainMessage();
                    message.obj = 0;
                    handler.sendMessage(message);
                }
                else {
                    Toast.makeText(getActivity(),e.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        return userList;
    }

    /*
    对 List 进行排序
     */
    private List<User> sort(List<User> mList){
        for (int i = 0 ; i < mList.size() ; i ++ ){
            for (int j = 1 ; j < mList.size() ; j++){
                if (mList.get(j).getmTotalTime() > mList.get(j - 1).getmTotalTime()){
                    User u = mList.get(j);
                    mList.set(j, mList.get(j-1));
                    mList.set(j-1, u);
                }
            }
        }
        return mList;
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
