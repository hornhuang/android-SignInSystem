package com.example.sht.homework.forums.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.Artical;
import com.example.sht.homework.forums.activities.ForumDetailActivity;

import java.util.List;

public class ArticalAdapter extends RecyclerView.Adapter<ArticalAdapter.ViewHoulder> {

    private List<Artical> mList;
    private Context context;

    private Bitmap bm=null;

    class ViewHoulder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mTitleText;
        private TextView mContentText;

        public ViewHoulder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.artical_image);
            mTitleText = itemView.findViewById(R.id.artical_title);
            mContentText = itemView.findViewById(R.id.artical_context);
        }
    }

    public ArticalAdapter(List<Artical> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHoulder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artical_list_item_view,
                viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHoulder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoulder viewHoulder, int i) {
        final Artical artical = mList.get(i);
        viewHoulder.mTitleText.setText(artical.getArticalTitleText());
        viewHoulder.mContentText.setText(artical.getArticalContextText());
        viewHoulder.mImage.setImageBitmap(artical.getArticlePhoto());
        viewHoulder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumDetailActivity.actionStart((AppCompatActivity) context, artical.getObjectId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    /**
//     * 增加数据
//     */
//    public void addData(int position, Artical artical) {
//        mList.add(position, artical);
//        notifyItemInserted(position);//注意这里
//    }
//
//    /**
//     * 移除数据
//     */
//    public void removeData(int position, Artical artical) {
//        mList.remove(position);
//        notifyItemRemoved(position);//注意这里
//    }

}
