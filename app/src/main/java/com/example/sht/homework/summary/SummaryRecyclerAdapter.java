package com.example.sht.homework.summary;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.bases.UserDetailActivity;
import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.bmobmanager.picture.FinalImageLoader;

import java.util.List;

public class SummaryRecyclerAdapter extends RecyclerView.Adapter<SummaryRecyclerAdapter.UserViewHolder> {

    private int num = 0;// 用于标记排名，<3 前三名加奖杯挂件

    private List<User> userList ;
    private Context context;
    private User user;
    private boolean flag = true;

    static class UserViewHolder extends RecyclerView.ViewHolder{
        /*
        数据成员
         */
        private ImageView head_icon;
        private ImageView rank_icon;
        private TextView user_name;
        private TextView user_group;
        private TextView user_motto;

        private String objectId;

        /*
        构造 item
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            head_icon  = itemView.findViewById(R.id.head_image);
            rank_icon  = itemView.findViewById(R.id.rank);
            user_name  = itemView.findViewById(R.id.user_name);
            user_group = itemView.findViewById(R.id.user_group);
            user_motto = itemView.findViewById(R.id.user_motto);
        }
    }

    public SummaryRecyclerAdapter(List<User> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.home_listview, viewGroup, false);
        context = viewGroup.getContext();
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
        user = userList.get(i);
        userViewHolder.user_name.setText(user.getFullname());
        userViewHolder.user_group.setText(user.getGroup());
        if (num<3){
            switchMedal(userViewHolder, num);
            num ++;
        }else {
            userViewHolder.rank_icon.setImageBitmap(null);
        }
        if (user.getMotto() == null){// 判断座右铭是否存在
            userViewHolder.user_motto.setText("该同学很懒，啥也没留下");
        }else {
            userViewHolder.user_motto.setText(user.getMotto());
        }
        if (user.getImageFile() == null){
            userViewHolder.head_icon.setImageResource(R.mipmap.app_icon);
        }else {
            new FinalImageLoader(userViewHolder.head_icon, user.getImageFile()).loadSmall();
        }
        userViewHolder.objectId = user.getObjectId();

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.Companion.anctionStart((AppCompatActivity) context, userViewHolder.objectId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // 排名前三名 加 金银铜 挂件
    private void switchMedal(UserViewHolder userViewHolder, int rank){
        switch (rank){
            case 0:
                userViewHolder.rank_icon.setImageResource(R.drawable.medal_gold);
                return ;

            case 1:
                userViewHolder.rank_icon.setImageResource(R.drawable.medal_silver);
                return ;

            case 2:
                userViewHolder.rank_icon.setImageResource(R.drawable.medal_bronze);
                return ;

            default:

                return ;

        }


    }

    /**
     * 增加数据
     */
    public void addData(int position, User user) {
        userList.add(position, user);
        notifyItemInserted(position);//注意这里
    }

    /**
     * 移除数据
     */
    public void removeData(int position) {
        userList.remove(position);
        notifyItemRemoved(position);//注意这里
    }

    public void reSetNum(){
        flag = true;
        num = 0;
    }

}
