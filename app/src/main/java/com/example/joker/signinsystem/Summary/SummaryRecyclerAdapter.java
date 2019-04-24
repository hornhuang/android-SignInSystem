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
        User user = userList.get(i);
        // userViewHolder.head_icon =
        userViewHolder.user_name.setText(user.getName());
        userViewHolder.user_group.setText(user.getGroup());
        userViewHolder.user_motto.setText(user.getMotto());
        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Click Items~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

//    private List<Map<String, Object>> data;
//    private LayoutInflater layoutInflater;
//    private Context context;
//    public SummaryRecyclerAdapter(Context context, List<Map<String, Object>> data){
//        this.context=context;
//        this.data=data;
//        this.layoutInflater=LayoutInflater.from(context);
//    }
//
//    /**
//     * 组件集合，对应list.xml中的控件
//     * @author Administrator
//     */
//    public final class Zujian{
//        public ImageView image;
//        public TextView title;
//        public Button view;
//        public TextView info;
//    }
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    /**
//     * 获得某一位置的数据
//     */
//    @Override
//    public Object getItem(int position) {
//        return data.get(position);
//    }
//
//    /**
//     * 获得唯一标识
//     */
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Zujian zujian=null;
//        if(convertView==null){
//            zujian=new Zujian();
//            //获得组件，实例化组件
//            convertView=layoutInflater.inflate(R.layout.home_listview, null);
//            zujian.image=(ImageView)convertView.findViewById(R.id.image);
//            zujian.title=(TextView)convertView.findViewById(R.id.title);
//            zujian.view=(Button)convertView.findViewById(R.id.view);
//            zujian.info=(TextView)convertView.findViewById(R.id.info);
//            convertView.setTag(zujian);
//        }else{
//            zujian=(Zujian)convertView.getTag();
//        }
//        //绑定数据
////        zujian.image.setBackgroundResource((Integer)data.get(position).get("image"));
//        zujian.title.setText((String)data.get(position).get("title"));
//        zujian.info.setText((String)data.get(position).get("info"));
//        return convertView;
//    }

}
