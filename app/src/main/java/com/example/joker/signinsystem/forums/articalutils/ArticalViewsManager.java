package com.example.joker.signinsystem.forums.articalutils;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.bmobmanager.AvatarLoader;
import com.example.joker.signinsystem.forums.activities.ForumDetailActivity;

public class ArticalViewsManager {

    // 对文章详情的真是信息进行导入
    public static void iniViews(Artical artical, ForumDetailActivity activity){
        new AvatarLoader(activity.getmIvPlaceholder(), artical.getArticalImageFile()).loadByFile();
        activity.getmArticalTitleText().setText(artical.getArticalTitleText());
        activity.getmArticalContentText().setText(artical.getArticalContextText());
        new AvatarLoader(activity.getmImToolbarWriterImage(), artical.getLinkUser()).loadByFile();
        activity.getmTvToolbarTitle().setText(artical.getLinkUser().getFullname());
        activity.getmTvToolbarMotto().setText(artical.getLinkUser().getMotto());
    }

}
