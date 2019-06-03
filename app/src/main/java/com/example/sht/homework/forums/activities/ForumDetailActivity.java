package com.example.sht.homework.forums.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.ActivityInterface;
import com.example.sht.homework.activities.BaseActivity;
import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.forums.articalutils.ArticalViewsManager;
import com.example.sht.homework.utils.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ForumDetailActivity extends BaseActivity implements ActivityInterface {

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private String objectId;

    private ImageView mIvPlaceholder; // 大图片--------------------
    private TextView mArticalTitleText;// title
    private TextView mArticalContentText;// 文章内容

    private LinearLayout mLlTitleContainer; // Title的LinearLayout

    private FrameLayout mFlTitleContainer; // Title的FrameLayout

    private AppBarLayout mAblAppBar; // 整个可以滑动的AppBar

    private CircleImageView mImToolbarWriterImage;
    private TextView mTvToolbarTitle; // 标题栏Title--------------------- username
    private TextView mTvToolbarMotto; // 作者座右铭

    private Toolbar mTbToolbar; // 工具栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        iniViews();

        mTbToolbar.setTitle("");
        objectId = getIntent().getStringExtra("objectId");

        // AppBar的监听
        mAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });

        initParallaxValues(); // 自动滑动效果
        iniData(); // 导入真实信息
        setMenuBarTransport();// 设置顶部菜单栏透明
    }

    @Override
    public void iniViews() {
        mIvPlaceholder = findViewById(R.id.main_iv_placeholder);
        mArticalTitleText = findViewById(R.id.artical_title) ;
        mArticalContentText = findViewById(R.id.artical_context) ;
        mLlTitleContainer = findViewById(R.id.main_ll_title_container);
        mFlTitleContainer = findViewById(R.id.main_fl_title);
        mAblAppBar = findViewById(R.id.main_abl_app_bar);
        mTvToolbarTitle = findViewById(R.id.main_tv_toolbar_title);
        mTvToolbarMotto = findViewById(R.id.artical_writer_motto);
        mImToolbarWriterImage = findViewById(R.id.small_photo);
        mTbToolbar = findViewById(R.id.main_tb_toolbar);
    }

    // 设置自动滑动的动画效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mIvPlaceholder.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFlTitleContainer.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mIvPlaceholder.setLayoutParams(petDetailsLp);
        mFlTitleContainer.setLayoutParams(petBackgroundLp);
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void iniData(){
        BmobQuery<Artical> bmobQuery = new BmobQuery<Artical>();
        bmobQuery.getObject(objectId, new QueryListener<Artical>() {
            @Override
            public void done(Artical object, BmobException e) {
                if(e==null){
                    ArticalViewsManager.iniForumDetailViews(object, ForumDetailActivity.this);
                }else{
                    MyToast.makeToast(ForumDetailActivity.this,"失败,请检查网络：" + e.getMessage());
                }
            }
        });
    }

    // 跳转
    public static void actionStart(AppCompatActivity activity, String objectId){
        Intent intent = new Intent(activity, ForumDetailActivity.class);
        intent.putExtra("objectId", objectId);
        activity.startActivity(intent);
    }

    public ImageView getmIvPlaceholder() {
        return mIvPlaceholder;
    }

    public TextView getmArticalTitleText() {
        return mArticalTitleText;
    }

    public TextView getmArticalContentText() {
        return mArticalContentText;
    }

    public CircleImageView getmImToolbarWriterImage() {
        return mImToolbarWriterImage;
    }

    public TextView getmTvToolbarTitle() {
        return mTvToolbarTitle;
    }

    public TextView getmTvToolbarMotto() {
        return mTvToolbarMotto;
    }

}
