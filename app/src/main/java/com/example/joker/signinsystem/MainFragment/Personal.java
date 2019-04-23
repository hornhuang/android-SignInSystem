package com.example.joker.signinsystem.MainFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.Thread.CountTimeThread;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.nestia.biometriclib.BiometricPromptManager;

import java.util.ArrayList;
import java.util.List;

public class Personal extends Fragment {

    private WifiManager wifiManager=null ;
    private WifiInfo wifiInfo=null;
    private Button begin;
    private String mac ="00:6b:8e:f6:99:d8";
    private boolean flag = false;
    private BiometricPromptManager mManager;

//    Handler handler2=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what==1){
//                begin.setText("计时开始:"+msg.obj);
//            }else if(msg.what==2){
//                begin.setText(""+msg.obj);
//            }
//        }
//    };

    private BroadcastReceiver mwifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("ss","ff");
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(wifiInfo.isConnected()){
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                String wifiSSID = wifiManager.getConnectionInfo()
                        .getSSID();
               // Toast.makeText(context, wifiSSID+"连接成功", 1).show();
            }else{
                 if(flag){
                     flag = false;
                     Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                     t.setToNow();
                     Toast.makeText(getActivity(), "结束时间"+"："+t.hour+":"+t.minute+":"+t.second,Toast.LENGTH_SHORT).show();
                     begin.setText("点击打卡");
                 }
            }
        }
    };

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         View view = inflater.inflate(R.layout.fragment_personal,container,false);

         IntentFilter myIntentFilter = new IntentFilter();
         myIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
         getActivity().registerReceiver(mwifiBroadcastReceiver,myIntentFilter);

         mManager = BiometricPromptManager.from(getActivity());
         begin = view.findViewById(R.id.begin);
         begin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                start();
             }
         });
        return view;
    }

    public void start(){
        wifiInfo  = wifiManager.getConnectionInfo();
        Log.i("SS", wifiInfo.toString());
        if(wifiInfo.getBSSID()==null){
            Toast.makeText(getActivity(),"please connect wifi",Toast.LENGTH_SHORT).show();
        }else if(!wifiInfo.getBSSID().equals(mac)){
            Toast.makeText(getActivity(),"wifi is wrong",Toast.LENGTH_SHORT).show();
        }else if(wifiInfo.getBSSID().equals(mac)){
            //Toast.makeText(getActivity(),"connect success",Toast.LENGTH_SHORT).show();
            if (mManager.isBiometricPromptEnable()) {
                mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
                    @Override
                    public void onUsePassword() {
                        Toast.makeText(getActivity(), "onUsePassword", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSucceeded() {
                        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                        t.setToNow();
                        begin.setText("开始计时"+"\n"+t.hour+":"+t.minute+":"+t.second);
                        //Toast.makeText(getActivity(), "onSucceeded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {

                        Toast.makeText(getActivity(), "onFailed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, String reason) {

                        Toast.makeText(getActivity(), "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(getActivity(), "onCancel", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            flag = true;

        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
