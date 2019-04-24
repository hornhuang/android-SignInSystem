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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joker.signinsystem.LandingRegistration.StartActivity;
import com.example.joker.signinsystem.MainActivity;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.Thread.CountTimeThread;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.nestia.biometriclib.BiometricPromptManager;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Personal extends Fragment {

    private WifiManager wifiManager=null ;
    private WifiInfo wifiInfo=null;
    private Button begin;
    private Toolbar toolbar = null;
    private String mac ="00:6b:8e:f6:99:d8";
    private boolean flag = false;
    private BiometricPromptManager mManager;

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
        setHasOptionsMenu(true);
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

        toolbar = view.findViewById(R.id.toolbar);
        initToolbar(toolbar, "签到", false);
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

    /**
     * Fragment中初始化Toolbar
     * @param toolbar
     * @param title 标题
     * @param isDisplayHomeAsUp 是否显示返回箭头
     */
    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_popmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat:

                Toast.makeText(getActivity(),"尚未推出敬请期待",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logOut:
                saveCode();
                startActivity(new Intent(getActivity(), StartActivity.class));
                getActivity().finish();
                break;

            default:
                break;

        }
        return true;
    }

    /*
 保存密码操作
  */
    private void saveCode(){
        String objectId = "";
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = getActivity().openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(objectId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
