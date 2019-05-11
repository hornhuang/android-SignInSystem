package com.example.sht.homework.forums.articalutils;

import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.bmobmanager.AvatarLoader;
import com.example.sht.homework.bmobmanager.pictures.SuperImageLoader;
import com.example.sht.homework.forums.activities.ForumDetailActivity;
import com.example.sht.homework.utils.MyToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class ArticalViewsManager {

    // 对文章详情的真是信息进行导入
    public static void iniForumDetailViews(Artical artical, final ForumDetailActivity activity){
        new SuperImageLoader(activity.getmIvPlaceholder(), artical).articalLoad();
        new AvatarLoader(activity.getmIvPlaceholder(), artical.getArticalImageFile()).loadByFile();
        activity.getmArticalTitleText().setText(artical.getArticalTitleText());
        activity.getmArticalContentText().setText(artical.getArticalContextText());

        // 查找
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(artical.getmWriterId(), new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if(e==null){
                    new SuperImageLoader(activity.getmImToolbarWriterImage(), object).userLoad();
                    activity.getmTvToolbarTitle().setText(object.getFullname());
                    activity.getmTvToolbarMotto().setText(object.getMotto());
                }else{
                    MyToast.makeToast(activity, "作者信息导入失败");
                }
            }
        });
    }

}
