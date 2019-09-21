package com.example.sht.homework.forums.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.ImageLoader;
import com.example.sht.homework.utils.MyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ForumEditActivity extends BaseActivity {

    private User mUser;

    private ImageView mCacel;
    private TextView mPublish;
    private EditText mEditTitle;
    private EditText mEditContent;
    private ImageView mArticalImage;
    private String path;
    private Artical mArtical;
    private String mObjectId;// 提交后生的 ObjectId 用于上传时命名图片
    // 获得程序最高可使用内存
    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artical);

        mUser = BmobUser.getCurrentUser(User.class);

        iniViews();
    }

    public static void actionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, ForumEditActivity.class);
        activity.startActivity(intent);
    }

    private void iniViews(){
        mCacel = findViewById(R.id.cancel);
        mPublish = findViewById(R.id.publish);
        mEditTitle = findViewById(R.id.edit_title);
        mEditContent = findViewById(R.id.edit_content);
        mArticalImage = findViewById(R.id.edit_image);

        mCacel.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mEditTitle.setOnClickListener(this);
        mEditContent.setOnClickListener(this);
        mArticalImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;

            case R.id.publish:
                publish();
                break;

            case R.id.edit_image:
                loadImage();
                break;
        }
    }

    private void publish(){
        if (path.equals("")){
            MyToast.makeToast(ForumEditActivity.this, "必须添加图片");
        }else {
            if (BmobUser.isLogin()){
                mArtical = new Artical();
                mArtical.setArticalTitleText(mEditTitle.getText().toString());
                mArtical.setArticalContextText(mEditContent.getText().toString());
                mArtical.setmWriterId(mUser.getObjectId());
                mArtical.setLinkUser(BmobUser.getCurrentUser(User.class));
                mArtical.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            mObjectId = s;
                            loadfile();
                        }else {
                            MyToast.makeToast(ForumEditActivity.this, "发布失败" + e.getMessage());
                        }
                    }
                });
            }else {
                MyToast.makeToast(ForumEditActivity.this, "请先登录");
            }
        }
    }

    private void loadfile(){
        final BmobFile bmobFile = new BmobFile(imageFactory(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    mArtical.setArticalImageFile(bmobFile);
                    mArtical.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                MyToast.makeToast(ForumEditActivity.this, "发布成功");
                                finish();
                            } else {
                                MyToast.makeToast(ForumEditActivity.this, "发布失败" + e.getMessage());
                            }
                        }
                    });
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                }else{
                    MyToast.makeToast(ForumEditActivity.this, "发布失败" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    private void loadImage(){
        if(ContextCompat.checkSelfPermission(ForumEditActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ForumEditActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    /*
    压缩路径下的文件
     */
    private File imageFactory(String picPath){
        BitmapFactory.Options o=new BitmapFactory.Options();
        Bitmap bitmap=BitmapFactory.decodeFile(picPath, o);
        bitmap=Bitmap.createScaledBitmap(bitmap, 700, 400, false);
        File root= getExternalCacheDir();
        File pic=new File(root, mObjectId + ".jpg");
        try {
            FileOutputStream fos=new FileOutputStream(pic);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pic;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
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

                        try {
                            Bitmap bitmap = ImageLoader.decodeSampleBitmap(path, mArticalImage);
                            mArticalImage.setImageBitmap(bitmap);
                        }catch (NullPointerException e){
                            Toast.makeText(ForumEditActivity.this,"该文件不存在",Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:

                break;
        }
    }

}
