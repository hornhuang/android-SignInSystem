package com.example.sht.homework.version;

import android.content.Intent;
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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.example.sht.homework.R;

public class VersionControlActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_AUTO = 1001;
    private static final int REQUEST_CHECK = 1002;
    private static final int REQUEST_SILENT = 1003;
    private static final int REQUEST_DELETE = 1004;

    private UpdateResponse updateResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_control);

        iniViews();

//        ButterKnife.bind(this);
//        //TODO 初始化，当控制台表出现后，注释掉此句
//        BmobUpdateAgent.initAppVersion();
        //TODO 设置仅WiFi环境更新
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        //TODO 设置更新监听器
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                BmobException e = updateInfo.getException();
                if (e == null) {
                    updateResponse = updateInfo;
                    Toast.makeText(VersionControlActivity.this, "检测更新返回：" + updateInfo.version + "-" + updateInfo.path, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VersionControlActivity.this, "检测更新返回：" + e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //TODO 设置对话框监听器
        BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

            @Override
            public void onClick(int status) {
                switch (status) {
                    case UpdateStatus.Update:
                        Toast.makeText(VersionControlActivity.this, "点击了立即更新按钮", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NotNow:
                        Toast.makeText(VersionControlActivity.this, "点击了以后再说按钮", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Close:
                        Toast.makeText(VersionControlActivity.this, "点击了对话框关闭按钮", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void iniViews(){
        CardView autoUpdate = findViewById(R.id.btn_auto_update);
        CardView checkUpdate = findViewById(R.id.btn_check_update);
        CardView downLoadUpdate = findViewById(R.id.btn_download_silent);
        CardView deleteUpdate = findViewById(R.id.btn_delete_apk);

        autoUpdate.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        downLoadUpdate.setOnClickListener(this);
        deleteUpdate.setOnClickListener(this);
    }


    /**
     * 检查权限
     *
     * @param requestCode
     */
    public void checkStoragePermissions(int requestCode) {
        List<String> permissions = new ArrayList<>();
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheckWrite != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            String[] missions = new String[]{};
            ActivityCompat.requestPermissions(this, permissions.toArray(missions), requestCode);
        } else {
            switch (requestCode) {
                case REQUEST_AUTO:
                    BmobUpdateAgent.update(this);
                    break;
                case REQUEST_CHECK:
                    BmobUpdateAgent.forceUpdate(this);
                    break;
                case REQUEST_SILENT:
                    BmobUpdateAgent.silentUpdate(this);
                    break;
                case REQUEST_DELETE:
                    BmobUpdateAgent.deleteResponse(updateResponse);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 检查授权结果
     *
     * @param grantResults
     * @return
     */
    public boolean checkResults(int[] grantResults) {
        if (grantResults == null || grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUTO:
                if (checkResults(grantResults)) {
                    BmobUpdateAgent.update(this);
                }
                break;
            case REQUEST_CHECK:
                if (checkResults(grantResults)) {
                    BmobUpdateAgent.forceUpdate(this);
                }
                break;
            case REQUEST_SILENT:
                if (checkResults(grantResults)) {
                    BmobUpdateAgent.silentUpdate(this);
                }
                break;
            case REQUEST_DELETE:
                if (checkResults(grantResults)) {
                    BmobUpdateAgent.deleteResponse(updateResponse);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto_update:
                checkStoragePermissions(REQUEST_AUTO);
                break;
            case R.id.btn_check_update:
                checkStoragePermissions(REQUEST_CHECK);
                break;
            case R.id.btn_download_silent:
                checkStoragePermissions(REQUEST_SILENT);
                break;
            case R.id.btn_delete_apk:
                checkStoragePermissions(REQUEST_DELETE);
                break;
            default:
                break;
        }
    }

    public static void anctionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, VersionControlActivity.class);
        activity.startActivity(intent);
    }
}
