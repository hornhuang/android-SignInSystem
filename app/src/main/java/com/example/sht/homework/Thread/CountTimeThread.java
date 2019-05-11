package com.example.sht.homework.Thread;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

public class CountTimeThread extends Thread {
    private int count = 0;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private Button begin;
    private Handler handler;
    private  String mac;
    public CountTimeThread(WifiManager wifiManager,WifiInfo wifiInfo,Button begin,Handler handler,String mac){
          this.wifiManager = wifiManager;
          this.wifiInfo = wifiInfo;
          this.begin = begin;
          this.handler = handler;
          this.mac = mac;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                wifiInfo =wifiManager.getConnectionInfo();
                if(wifiInfo.getBSSID()==null||!wifiInfo.getBSSID().equals(mac)){
                    Message msg=new Message();
                    msg.what=2;
                    msg.obj="点击签到";
                    handler.sendMessage(msg);
                    break;
                }
                count++;
                //使用以下几种方式都可以
                Message msg=new Message();
                msg.what=1;
                msg.obj=count;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
