package com.example.joker.signinsystem.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.forums.activities.MyArticalActivity;
import com.example.joker.signinsystem.fragments.Personal;
import com.example.joker.signinsystem.fragments.Ranking;
import com.example.joker.signinsystem.fragments.Summary;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.bmobmanager.AvatarLoader;
import com.example.joker.signinsystem.utils.MyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private User user;// 获取登录成功后的本地用户信息
    private CircleImageView mUserimageView;
    private TextView mUserName;
    private TextView mUserMotto;
    private LinearLayout mArticleLayout;

    private Bitmap bitmap;//从相册获得图片
    private BottomNavigationView navigation;
    private FragmentManager fragmentManager;
    private String path ;//图片路径
    private Fragment mContent = new Fragment();// 记录下当前碎片 由于替换

    private Fragment mPerson;
    private Fragment mRanking;
    private Fragment mSummary;
    private FragmentTransaction transaction;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if((Integer)msg.obj==0){
                mUserimageView.setImageBitmap(bitmap);
            }if (msg.what == 0x0){
                ((Personal)mPerson).getmTips().setText("");
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchContent(mPerson);
                    return true;
                case R.id.navigation_dashboard:
                    switchContent(mRanking);
                    return true;
                case R.id.navigation_ranking:
                    switchContent(mSummary);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //底部选择碎片切换
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Toolbar代替ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = BmobUser.getCurrentUser(User.class);
        iniSideView();
    }

    /*
    侧拉框用户个人信息设置
     */
    private void iniSideView(){
        mUserimageView = findViewById(R.id.user_image);
        mUserName = findViewById(R.id.user_name);
        mUserMotto = findViewById(R.id.user_motto);
        mArticleLayout = findViewById(R.id.my_article);
        mUserimageView.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mUserMotto.setOnClickListener(this);
        mArticleLayout.setOnClickListener(this);

        mPerson = new Personal();
        mRanking = new Ranking();
        mSummary = new Summary();
        fragmentManager = getSupportFragmentManager();
        mContent = mRanking;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment, mContent).commit();
        navigation.setSelectedItemId(R.id.navigation_home);

        new Thread(){
            @Override
            public void run() {
                while (user == null){
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

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

    /**
     * 修改显示的内容 不会重新加载
     * to 下一个fragment
     * mContent 当前的fragment
     */
    private void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) { // 判断是否被add过
                // 隐藏当前的fragment，将 下一个fragment 添加进去
                transaction.hide(mContent).add(R.id.fragment, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个fragment
                transaction.hide(mContent).show(to).commit();
            }
            mContent = to;
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

            case R.id.my_theme:
                // do 。。。 ();
                break;

            case R.id.my_article:
                MyArticalActivity.actionStart(MainActivity.this);
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
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private File imageFactory(String picPath){
        BitmapFactory.Options o=new BitmapFactory.Options();
        Bitmap bitmap=BitmapFactory.decodeFile(picPath, o);
        bitmap=Bitmap.createScaledBitmap(bitmap, 400, 400, false);
        File root = getExternalCacheDir();
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

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
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
    private void changeUserName(final String name){
        user.setName(name);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "用户名更新成功", Toast.LENGTH_SHORT).show();
                    mUserName.setText(name);
                } else {
                    MyToast.makeToast(MainActivity.this, "用户名更新成功");
                }
            }
        });
    }

    /*
    数据库更新座右铭
     */
    private void changeMotto(final String motto){
        user.setMotto(motto);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "座右铭更新成功", Toast.LENGTH_SHORT).show();
                    mUserMotto.setText(motto);
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
