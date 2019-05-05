package com.example.joker.signinsystem;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joker.signinsystem.fragments.Personal;
import com.example.joker.signinsystem.fragments.Ranking;
import com.example.joker.signinsystem.fragments.Summary;
import com.example.joker.signinsystem.activities.ChangeUserIfo;
import com.example.joker.signinsystem.activities.StartActivity;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.bmobmanager.AvatarLoader;
import com.example.joker.signinsystem.utils.MyDate;
import com.example.joker.signinsystem.utils.SDKFileManager;
import com.example.joker.signinsystem.utils.Toasty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";
    private User user;// 获取登录成功后的本地用户信息
    private CircleImageView mUserimageView;
    private TextView mUserName;
    private TextView mUserMotto;
    //从相册获得图片
    private Bitmap bitmap;
    //图片路径
    private String path ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if((Integer)msg.obj==0){
                mUserimageView.setImageBitmap(bitmap);
            }
            super.handleMessage(msg);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new Personal());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new Ranking());
                    return true;
                case R.id.navigation_ranking:
                    replaceFragment(new Summary());
                    return true;
            }
            return false;
        }
    };

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第二：默认初始化
        Bmob.initialize(this, APPID);

        //底部选择碎片切换
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Toolbar代替ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userId = SDKFileManager.getObjectId(this);
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    user = BmobUser.getCurrentUser(User.class);
                    iniSideView();

                    //先用首页碎片替换碎片
                    replaceFragment(new Personal());
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                } else {
                    Toast.makeText(MainActivity.this, "登录过期，请重新登录" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                    finish();
                }
            }
        });
    }

    /*
    侧拉框用户个人信息设置
     */
    private void iniSideView(){
        mUserimageView = findViewById(R.id.user_image);
        mUserName = findViewById(R.id.user_name);
        mUserMotto = findViewById(R.id.user_motto);
        mUserimageView.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mUserMotto.setOnClickListener(this);

        mUserName.setText(user.getName());//
        if (user.getImageFile() == null){
            mUserimageView.setImageResource(R.mipmap.app_icon);
        }else {
            new AvatarLoader(mUserimageView, user).load();
        }
        if (user.getMotto() == null){
            mUserMotto.setText("该同学很懒，啥也没留下");
        }else {
            mUserMotto.setText(user.getMotto());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_image:
                changeHeadImage();
                break;

            case R.id.user_name:
                simple();
                break;

            case R.id.user_motto:
                simple();
                break;

            default:

                break;

        }
    }

    /*
    更换侧拉框头像
     */
    private void changeHeadImage(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    /*定义一个Handler，定义延时执行的行为*/
    public void chnageImage(){
        final String picPath = path;
        final BmobFile bmobFile = new BmobFile(imageFactory(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    user.setImageFile(bmobFile);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "头像已更新", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                    });
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    new Thread(){
                        @Override
                        public void run() {
                            while ( bitmap == null ){
                                bitmap = BitmapFactory.decodeFile(picPath);
                            }
                            Message message = handler.obtainMessage();
                            message.obj = 0;
                            handler.sendMessage(message);
                        }
                    }.start();
                }else{
                    Toast.makeText(MainActivity.this, "设置失败，请重新选择", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /*
    压缩路径下的文件
     */
    private File imageFactory(String picPath){
        BitmapFactory.Options o=new BitmapFactory.Options();
        Bitmap bitmap=BitmapFactory.decodeFile(picPath, o);
        bitmap=Bitmap.createScaledBitmap(bitmap, 400, 400, false);
        File root= getExternalCacheDir();
        File pic=new File(root,"test.jpg");
        try {
            FileOutputStream fos=new FileOutputStream(pic);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pic;
    }

    /*
    点击侧拉框用户信息
    用户点击信息进行修改
     */
    private void simple(){
        String[] items = new String[]{
                "更换用户名",
                "跟换座右铭",
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("您想执行操作")//设置对话框 标题
                .setIcon(R.drawable.seek)//设置图标
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, ChangeUserIfo.class);
                        switch (which){
                            case 0:
                                intent.putExtra("function", "name");
                                startActivityForResult(intent,0);
                                break;

                            case 1:
                                intent.putExtra("function", "motto");
                                startActivityForResult(intent, 1);
                                break;

                            default:
                                // do nothing...
                                break;
                        }
                        return;
                    }
                });
        setPositiveButton(builder);//add 'yes' Button to AlertDialog
        setNegativeButton(builder)//add 'no' Button to AlertDialog
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK ){
                    assert data != null;
                    changeUserName(data.getStringExtra("username"));
                }
                break;

            case 1:
                if (resultCode == RESULT_OK ) {
                    assert data != null;
                    changeMotto(data.getStringExtra("usermotto"));
                }
                break;

            case 2://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        bitmap = BitmapFactory.decodeFile(path,options);
                        chnageImage();
                        Toast.makeText(MainActivity.this,path,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:

                break;
        }
    }

    /*
    数据库更新用户名
     */
    private void changeUserName(String name){
        user.setName(name);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "用户名更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.Toasty(MainActivity.this, "用户名更新成功");
                }
            }
        });
    }

    /*
    数据库更新座右铭
     */
    private void changeMotto(String motto){
        user.setMotto(motto);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "座右铭更新成功", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

    /*
    对话框确定按钮
     */
    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder){
        // use 'setPositiveButton' method to add 'yes' Button
        return builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"you click '确定' button ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    对话框取消按钮
     */
    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder){
        // use 'setPositiveButton' method to add 'no' Button
        return builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"you click 'no' button ",Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /*
    get() & set()
     */
    public User getUser(){
        return user;
    }

}
