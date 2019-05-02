package com.example.joker.signinsystem.Summary;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joker.signinsystem.MainFragment.Summary;
import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.User;

import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class SummaryRecyclerAdapter extends RecyclerView.Adapter<SummaryRecyclerAdapter.UserViewHolder> {
    /*
    列表成员
     */
    private List<User> userList ;
    private Context context;

    static class UserViewHolder extends RecyclerView.ViewHolder{
        /*
        数据成员
         */
        private ImageView head_icon;
        private TextView user_name;
        private TextView user_group;
        private TextView user_motto;

        /*
        构造 item
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            head_icon = itemView.findViewById(R.id.head_image);
            user_name = itemView.findViewById(R.id.user_name);
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
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        final User user = userList.get(i);
        // userViewHolder.head_icon =
        userViewHolder.user_name.setText(user.getFullname());
        userViewHolder.user_group.setText(user.getGroup());

        if (user.getMotto() == null){// 判断座右铭是否存在
            userViewHolder.user_motto.setText("该同学很懒，啥也没留下");
        }else {
            userViewHolder.user_motto.setText(user.getMotto());
        }
        if (user.getHeadIcon() == null){
            userViewHolder.head_icon.setImageResource(R.mipmap.app_icon);
        }else {
            userViewHolder.head_icon.setImageBitmap(user.getHeadIcon());
        }

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Click " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
