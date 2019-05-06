package com.example.joker.signinsystem.forums.activities;

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

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.activities.BaseActivity;
import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.baseclasses.User;
import com.example.joker.signinsystem.utils.MyLog;
import com.example.joker.signinsystem.utils.MyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditArticalActivity extends BaseActivity {

    private String APPID = "bd4814e57ed9c8f00aa0d119c5676cf9";
    private ImageView mCacel;
    private TextView mPublish;
    private EditText mEditTitle;
    private EditText mEditContent;
    private ImageView mArticalImage;
    private String path;
    private Artical artical;
    private String objectId;// 提交后生的 ObjectId 用于上传时命名图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artical);

        Bmob.initialize(this, APPID);

        iniViews();
    }

    public static void actionStart(AppCompatActivity activity){
        Intent intent = new Intent(activity, EditArticalActivity.class);
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
            MyToast.makeToast(EditArticalActivity.this, "必须添加图片");
        }else {
            if (BmobUser.isLogin()){
                artical = new Artical();
                artical.setArticalTitleText(mEditTitle.getText().toString());
                artical.setArticalContextText(mEditContent.getText().toString());
                artical.setLinkUser(BmobUser.getCurrentUser(User.class));
                artical.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            objectId = s;
                            loadfile();
                        }else {
                            MyToast.makeToast(EditArticalActivity.this, "发布失败" + e.getMessage());
                        }
                    }
                });
            }else {
                MyToast.makeToast(EditArticalActivity.this, "请先登录");
            }
        }
    }

    private void loadfile(){
        final BmobFile bmobFile = new BmobFile(imageFactory(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    artical.setArticalImageFile(bmobFile);
                    artical.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                MyToast.makeToast(EditArticalActivity.this, "发布成功");
                                finish();
                            } else {
                                MyToast.makeToast(EditArticalActivity.this, "发布失败" + e.getMessage());
                            }
                        }
                    });
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                }else{
                    MyToast.makeToast(EditArticalActivity.this, "发布失败" + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    private void loadImage(){
        if(ContextCompat.checkSelfPermission(EditArticalActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditArticalActivity.this,new String[]{
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
        File pic=new File(root,objectId + ".jpg");
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

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
                        mArticalImage.setImageBitmap(bitmap);
                        Toast.makeText(EditArticalActivity.this,path,Toast.LENGTH_SHORT).show();
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
