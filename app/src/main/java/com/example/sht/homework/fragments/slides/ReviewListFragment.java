package com.example.sht.homework.fragments.slides;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.PushPlanActivity;
import com.example.sht.homework.baseclasses.Plan;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.fragments.slides.review.PlanAdapter;
import com.example.sht.homework.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ReviewListFragment extends Fragment {

    private RecyclerView recyclerView;

    private FloatingActionButton fab;

    private List<Plan> mPlanLists;

    private PlanAdapter adapter;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushPlanActivity.actionStart(getActivity());
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
                        adapter.notifyDataSetChanged();
                    } else {
                        MyToast.makeToast(getActivity(), "失败，请检查网络" + e.getMessage());
                    }
                }
            });
        }else {
            MyToast.makeToast(getActivity(), "请先登录");
        }

    }
}
