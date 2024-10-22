package com.example.sht.homework.fragments.slides;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.activities.PushPlanActivity;
import com.example.sht.homework.baseclasses.Plan;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.fragments.BaseFragment;
import com.example.sht.homework.fragments.slides.review.PlanAdapter;
import com.example.sht.homework.utils.Dater;
import com.example.sht.homework.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ReviewListFragment extends BaseFragment {

    private final String TAG = "ReviewListFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    private List<Plan> mPlanLists;

    private PlanAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            resetPlanLists(mPlanLists);
            adapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        iniViews(view);
        iniRecyclerView();
        return view;
    }

    private void iniViews(View view){
        recyclerView = view.findViewById(R.id.recycler);
        fab          = view.findViewById(R.id.fab);
        mRefreshLayout = view.findViewById(R.id.refresh);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushPlanActivity.actionStart(getActivity());
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    private void iniRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        mPlanLists = new ArrayList<>();
        adapter = new PlanAdapter(mPlanLists, getActivity());
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData(){
        if (BmobUser.isLogin()) {
            BmobQuery<Plan> query = new BmobQuery<>();
            query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("user");
            query.findObjects(new FindListener<Plan>() {
                @Override
                public void done(List<Plan> object, BmobException e) {
                    if (e == null) {
                        mPlanLists.clear();
                        mPlanLists.addAll(object);
                        handler.sendMessage(new Message());
                    } else {
                        MyToast.makeToast(getActivity(), "失败，请检查网络" + e.getMessage());
                    }
                }
            });
        }else {
            MyToast.makeToast(getActivity(), "请先登录");
        }

    }

    private void resetPlanLists(List<Plan> initialPlans){
        List<Plan> resetList = new ArrayList<>();
        for (Plan plan : initialPlans){
            int days = Dater.getDiscrepantDays(plan.getDate(), new Date());
            if( days == 1 || days == 2 || days == 4 || days == 5 || days == 15  ){
                resetList.add(plan);
                if (days > 15){
                    removeDBPlan(plan);
                }
            }
        }
        mPlanLists.clear();
        mPlanLists.addAll(resetList);
    }

    private void removeDBPlan(final Plan expiredPlan){
        new Thread(){
            @Override
            public void run() {
                super.run();
                expiredPlan.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.d(TAG, "删除成功:" + expiredPlan.getUpdatedAt());
                        }else{
                            Log.d(TAG,"删除失败：" + e.getMessage());
                        }
                    }

                });
            }
        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRefreshLayout.setRefreshing(true);
        getData();
    }
}
