package com.example.sht.homework.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sht.homework.R;

public class ChangeUserIfo extends AppCompatActivity {

    /*
    控件成员
     */
    private TextView mToolBarFunction;
    private EditText mEditText;
    private Button mFinishButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_ifo);
        iniViews();
    }

    private void iniViews(){
        mToolBarFunction = findViewById(R.id.toolbar_function);
        mFinishButton = findViewById(R.id.finish);
        mEditText = findViewById(R.id.edit);
        mFinishButton = findViewById(R.id.finish);

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChange();
            }
        });

        intent = getIntent();
        switch (intent.getStringExtra("function")){
            case "name":
                mToolBarFunction.setText("修改用户名");
                mEditText.setHint("修改用户名");
                break;

            case "motto":
                mToolBarFunction.setText("修改座右铭");
                mEditText.setHint("修改座右铭");
                break;

            default:

                break;
        }
    }

    private void finishChange(){
        switch (intent.getStringExtra("function")){
            case "name":
                intent = new Intent();
                intent.putExtra("username", mEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case "motto":
                intent = new Intent();
                intent.putExtra("usermotto", mEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:

                break;
        }
    }

}
