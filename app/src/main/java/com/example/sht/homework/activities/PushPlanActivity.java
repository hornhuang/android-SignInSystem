package com.example.sht.homework.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.Plan;
import com.example.sht.homework.baseclasses.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PushPlanActivity extends AppCompatActivity {

    private EditText mCourseEdit;

    private TextView mDateText;

    private EditText mUriLink;

    private Button   mPushBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_plan);

        iniViews();
    }

    private void iniViews(){
        mCourseEdit = findViewById(R.id.course);
        mDateText   = findViewById(R.id.date);
        mUriLink    = findViewById(R.id.url);
        mPushBtn    = findViewById(R.id.push);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        mDateText.setText(date);

        mPushBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushPlan();
            }
        });
    }

    private void pushPlan(){
        Plan plan = new Plan();
        plan.setCourse(mCourseEdit.getText().toString());
        plan.setDate(new Date());
        plan.setUri(mUriLink.getText().toString());
        plan.setUser(BmobUser.getCurrentUser(User.class));
        plan.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Toast.makeText(PushPlanActivity.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(PushPlanActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, PushPlanActivity.class);
        activity.startActivity(intent);
    }
}
