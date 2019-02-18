package com.example.joker.signinsystem.MainFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.joker.signinsystem.LandingRegistration.User;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.Summary.ListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class Summary extends Fragment{

    private User user[] = new User[1000];

    private int n ;

    public static String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";

    Button button01 ;

    private int num = 10;

    private ListView listView;

    private ListAdapter listAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if((Integer)msg.obj==0){
                 List<Map<String, Object>> list=getData();

                listView.setAdapter(new ListViewAdapter(getActivity(), list));


            }
            super.handleMessage(msg);
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_summary , container, false);
//        thread.start();
        //第二：默认初始化
        Bmob.initialize(getActivity(),APPID);
        button01 = (Button) view.findViewById(R.id.button01);
        listView = (ListView)view.findViewById(R.id.listview);
        button01.setClickable(true);
        final List<Map<String, Object>> list=getData();
        listView.setAdapter(new ListViewAdapter(getActivity(), list));
        chnage();
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, Object>> list=getData();
                listView.setAdapter(new ListViewAdapter(getActivity(), list));
            }
        });
        return view;
    }


    /*定义一个Handler，定义延时执行的行为*/
    public  void chnage(){
        new Thread(){
            @Override
            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                while ( getData().size() == 0 ){

                }
                Message message = handler.obtainMessage();
               message.obj = 0;
               handler.sendMessage(message);
            }
        }.start();

    }



    public List<Map<String, Object>> getData(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.findObjects(new FindListener<User>() {

            @Override

            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    n = list.size();
                    System.out.println(n);
                    for (int i = 0; i < n; i++) {
                        user[i] = list.get(i);
                    }
                    Arrays.sort(user, 0, n);		// 新加了一个排序函数
                    String str = "";
                }
                else {
                    System.out.println(e.getErrorCode());
                }
            }

        });

        for (int i = 0; i < n; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", R.drawable.thumb);
            map.put("title", user[i].getUsername());
            map.put("info", user[i] .getMobilePhoneNumber());
            list.add(map);
        }
        Log.i("ss",list.size()+"ddd");
        return list;
    }


//    private void loadData() {
//
//        BmobQuery<User> bmobQuery = new BmobQuery<User>();
//
//        bmobQuery.findObjects(new FindListener<User>() {
//
//            @Override
//
//            public void done(List<User> list, BmobException e) {
//
//                if (e == null) {
//                    n = list.size();
//                    System.out.println(n);
//                    for (int i = 0; i < n; i++) {
//                        user[i] = list.get(i);
//                    }
//                    Arrays.sort(user, 0, n);		// 新加了一个排序函数
//                    String str = "";
//                    for (int i = 0; i < n; i++) {
//                        str = str + user[i].getName() + " " + user[i].getId() + " " + user[i].getLineId() + "号线\n";
//                        System.out.println(user[i].getName() + "," + user[i].getId() + "," + user[i].getLineId());
//                    }
//                    textView.setText(str);
//                }
//                else {
//                    System.out.println(e.getErrorCode());
//                }
//            }
//
//        });
//
//    }


}
