package com.example.sht.homework.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.activities.MainActivity;
import com.example.sht.homework.activities.StartActivity;
import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.AppContext;
import com.example.sht.homework.utils.Dater;
import com.example.sht.homework.utils.MyToast;
import com.nestia.biometriclib.BiometricPromptManager;

import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**2018.*.*
 * 指纹签到
 * @author liujianjun
 */
public class Personal extends Fragment {

    private final int GPS_REQUEST_CODE = 10;

    private User user ;
    private Button begin;
    private TextView mTips;
    private WifiManager wifiManager=null ;
    private WifiInfo wifiInfo=null;
    private Toolbar toolbar = null;
    private final String mac213 ="00:6b:8e:f6:99:d8";
    private String mac201_xiaomi ="ec:41:18:1e:5c:5f";
    private String mac201newThinker ="8c:f2:28:27:1a:fc";
    private boolean flag = false;
    private BiometricPromptManager mManager;

    private boolean isRunning = false;//是否已经开始计时
    private int time; // 开始签到时的时间
    private BiometricPromptManager.OnBiometricIdentifyCallback biometricIdentifyCallback;
    private Handler handler;
    private Thread thread = new Thread(){
        @Override
        public void run() {
            while (true){
                handler.post(runnable);
                try {
                    sleep(60000);// 每一分钟看下事件
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time ++;
                if (time >= 60){//
                    biometricIdentifyCallback.onCancel();
                }
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTips.setText("已打卡： " + time + " 分钟");
        }
    };

    private BroadcastReceiver mwifiBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(wifiInfo.isConnected()){
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                String wifiSSID = wifiManager.getConnectionInfo()
                        .getSSID();
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

        wifiManager = (WifiManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        View view = inflater.inflate(R.layout.fragment_personal,container,false);

        iniViews(view);

        user = ((MainActivity) getActivity()).getUser();
        matchYesterDayFlag();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mwifiBroadcastReceiver,myIntentFilter);// 会报错 但删了会奔溃

        FingerprintManager fingerprintManager = getContext().getSystemService(FingerprintManager.class);
        if (fingerprintManager.hasEnrolledFingerprints()){
            mManager = BiometricPromptManager.from(getActivity());// 没有指纹就会崩溃
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    10);
        }else {
            jumpDialog("提示", "本机无指纹功能，无法使用签到");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    10);
        }
        begin = view.findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openGPSSettings();
             }
         });

        toolbar = view.findViewById(R.id.toolbar);
        initToolbar(toolbar, "签到", false);

        return view;
    }

    private void jumpDialog(String title, String msg){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    private void iniViews(View view){
        mTips = view.findViewById(R.id.person_tips);

        handler = new Handler();

    }

    private void matchYesterDayFlag(){
        if (Dater.getWeekOfDate() != user.getmYesturdayFlag()){
            switch (user.getmYesturdayFlag()){
                case 1:
                    user.setmMondatTime(0);
                    break;
                case 2:
                    user.setmTuesdayTime(0);
                    break;
                case 3:
                    user.setmWednesdayTime(0);
                    break;
                case 4:
                    user.setmThursdayTime(0);
                    break;
                case 5:
                    user.setmFridayTime(0);
                    break;
                case 6:
                    user.setmSaturdayTime(0);
                    break;
                case 7:
                    user.setmSundayTime(0);
                    break;
                default:

                    break;
            }
            user.setmYesturdayFlag(Dater.getWeekOfDate());
        }
    }

    public void start(){
        wifiInfo  = wifiManager.getConnectionInfo();
        Toast.makeText(getActivity(), wifiInfo.getBSSID(),Toast.LENGTH_SHORT).show();
        if(wifiInfo.getBSSID()==null){
            Toast.makeText(getActivity(),"请打开 w+ifi",Toast.LENGTH_SHORT).show();
        }else if(wifiInfo.getBSSID().equals(mac213)||wifiInfo.getBSSID().equals(mac201_xiaomi)||wifiInfo.getBSSID().equals(mac201newThinker)||
                wifiInfo.getBSSID().equals(mac201_xiaomi)|| wifiInfo.getBSSID().equals(mac201newThinker)){
            if (mManager.isBiometricPromptEnable()) {
                biometricIdentifyCallback = new BiometricPromptManager.OnBiometricIdentifyCallback() {
                    @Override
                    public void onUsePassword() {
                        Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSucceeded() {
                        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                        t.setToNow();
                        begin.setText("开始计时"+"\n"+t.hour+":"+t.minute+":"+t.second);
                        isRunning = true;
                        thread.start();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getActivity(), "请按指纹", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int code, String reason) {
                        Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Time t = new Time();
                        t.setToNow();
                        Toast.makeText(getActivity(), "打卡结束,正在更新信息", Toast.LENGTH_SHORT).show();
                        reSetUser();
                        begin.setText("点击打卡");
                        isRunning = false;
                    }
                };
                mManager.authenticate(biometricIdentifyCallback);
            }
            flag = true;

        }else {// 与实验室 wifi ID 进行匹配 213
            Toast.makeText(getActivity(),"请连接实验室 wifi",Toast.LENGTH_SHORT).show();
        }
    }

    // 重置 user 的时间
    private void reSetUser(){
        switch (user.getmYesturdayFlag()){
            case 1:
                user.setmMondatTime(user.getmMondatTime() + time);
                break;
            case 2:
                user.setmTuesdayTime(user.getmTuesdayTime() + time);
                break;
            case 3:
                user.setmWednesdayTime(user.getmWednesdayTime() + time);
                break;
            case 4:
                user.setmThursdayTime(user.getmThursdayTime() + time);
                break;
            case 5:
                user.setmFridayTime(user.getmFridayTime() + time);
                break;
            case 6:
                user.setmSaturdayTime(user.getmSaturdayTime() + time);
                break;
            case 7:
                user.setmSundayTime(user.getmSundayTime() + time);
                break;
            default:

                break;
        }
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "信息已更新", Toast.LENGTH_SHORT).show();
                } else {
                    MyToast.makeToast((AppCompatActivity) getActivity(), "信息提交失败!");
                }
            }
        });
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
            case R.id.logOut:
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), StartActivity.class));
                getActivity().finish();
                break;

            default:
                break;

        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRunning){
            biometricIdentifyCallback.onCancel();
        }
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

    public TextView getmTips() {
        return mTips;
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        final LocationManager locationManager = (LocationManager) AppContext.getInstance().getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            if (isRunning){
                biometricIdentifyCallback.onCancel();
            }else {
                IntentFilter myIntentFilter = new IntentFilter();
                myIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                getActivity().registerReceiver(mwifiBroadcastReceiver,myIntentFilter);
                start();
            }
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
                                    MyToast.makeToast(getActivity(), "无法使用该功能");
                                }
                            })

                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_REQUEST_CODE:
                openGPSSettings();
                break;
            default:
                break;
        }
    }
}
