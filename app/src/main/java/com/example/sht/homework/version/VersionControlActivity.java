package com.example.sht.homework.version;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.Update;
import com.example.sht.homework.utils.MyLog;
import com.example.sht.homework.utils.MyToast;

public class VersionControlActivity extends AppCompatActivity implements View.OnClickListener {

    private int mProgress;
    private String url, code1, text;
    private int code;
    private boolean mIsCancel;
    private Context mContext = VersionControlActivity.this;
    private String mSavePath, mVersion_name;

    private ProgressBar mProgressBar;
    private Dialog mDownloadDialog;

    // 接收消息
    private Handler mUpdateProgressHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    // 设置进度条
                    mProgressBar.setProgress(mProgress);
                    break;
                case 2:
                    // 隐藏当前下载对话框
                    mDownloadDialog.dismiss();
                    // 安装 APK 文件
                    installAPK();
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_control);

        iniViews();

    }

    private void iniViews(){
        CardView autoUpdate = findViewById(R.id.btn_auto_update);
//        CardView checkUpdate = findViewById(R.id.btn_check_update);
//        CardView downLoadUpdate = findViewById(R.id.btn_download_silent);
//        CardView deleteUpdate = findViewById(R.id.btn_delete_apk);

        autoUpdate.setOnClickListener(this);
//        checkUpdate.setOnClickListener(this);
//        downLoadUpdate.setOnClickListener(this);
//        deleteUpdate.setOnClickListener(this);
    }

    //查询单条数据
    public void querySingleData() {
        BmobQuery<Update> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject("xDzXVVVq", new QueryListener<Update>() {
            @Override
            public void done(Update object, BmobException e) {
                if (e == null) {
                    url = object.getapkUrl();
                    code1 = object.getCode();
                    text = object.getText();
                    System.out.println("APK更新地址：" + url);
                    System.out.println("版本号：" + code1);
                    System.out.println("更新内容" + text);
                    MyToast.makeToast(VersionControlActivity.this, "正在检测版本更新");
                    check(code1);
                } else {
                    MyToast.makeToast(VersionControlActivity.this, "检测失败：" +
                            e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    //判断版本大小
    public void check(String code1) {
        code = APKVersionCodeUtils.getVersionCode(this) ;
        int i = Integer.valueOf(code1);
        if (i > code) {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)//设置标题的图片
                .setTitle("检查到新版本")//设置对话框的标题
                .setMessage(text)//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //postUrl(url);
                        mIsCancel=false;
                        //展示对话框
                        showDownloadDialog(url);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    // 显示正在下载对话框
    protected void showDownloadDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("下载中");
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        mProgressBar = view.findViewById(R.id.id_progress);
        builder.setView(view);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
                // 设置下载状态为取消
                mIsCancel = true;
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();

        // 下载文件
        downloadAPK();
    }

    // 开启新线程下载apk文件
    private void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        String sdPath = Environment.getExternalStorageDirectory() + "/";
                        // 文件保存路径
                        mSavePath = sdPath;

                        File dir = new File(mSavePath);
                        if (!dir.exists()){
                            dir.mkdir();
                        }
                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        int length = conn.getContentLength();
                        mVersion_name = "app_debug.apk";
                        File apkFile = new File(
                                getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                                mVersion_name);
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while (!mIsCancel){
                            int numread = is.read(buffer);
                            count += numread;
                            // 计算进度条的当前位置
                            mProgress = (int) (((float)count/length) * 100);
                            // 更新进度条
                            mUpdateProgressHandler.sendEmptyMessage(1);

                            // 下载完成
                            if (numread < 0){
                                mUpdateProgressHandler.sendEmptyMessage(2);
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                        fos.close();
                        is.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
     * 下载到本地后执行安装
     */
    protected void installAPK() {
        new Thread(){
            @Override
            public void run() {
                try {
                    File apkFile = new File(
                            getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                            mVersion_name);
                    if (!apkFile.exists()){
                        return;
                    }
                    if(Build.VERSION.SDK_INT>=24) {//判读版本是否在7.0以上
                        Uri apkUri = FileProvider.getUriForFile(
                                getApplicationContext(),
                                "com.example.sht.homework.fileProvider",
                                apkFile);//在AndroidManifest中的android:authorities值
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                        // 重点 要先 set 后 add 因为 set 会把 add 的东西全清空掉！！！
//                        install.addCategory(Intent.CATEGORY_DEFAULT);
                        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        sleep(3000);
                        startActivity(install);
                    } else{
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        sleep(3000);
                        startActivity(install);
                    }
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto_update:
                querySingleData();
                break;
//            case R.id.btn_check_update:
//
//                break;
//            case R.id.btn_download_silent:
//
//                break;
//            case R.id.btn_delete_apk:
//
//                break;

            default:

                break;
        }
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, VersionControlActivity.class);
        activity.startActivity(intent);
    }
}
