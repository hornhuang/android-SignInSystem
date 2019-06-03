package com.example.sht.homework.forums.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
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

import java.util.ArrayList;
import java.util.List;

public class ArticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Artical> mList;
    private Context context;

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示

    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

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

    // 底部footView的ViewHolder，用以缓存findView操作
    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public ArticalAdapter(List<Artical> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        context = viewGroup.getContext();
        if (viewType == normalType) {
            view = LayoutInflater.from(context).inflate(R.layout.artical_list_item_view,
                    viewGroup, false);
            return new ViewHoulder(view);
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.foot_view,
                    viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHoulder) {
            final Artical artical = mList.get(i);
            ViewHoulder viewHoulder = (ViewHoulder) viewHolder;
            viewHoulder.mTitleText.setText(artical.getArticalTitleText());
            viewHoulder.mContentText.setText(artical.getArticalContextText());
            viewHoulder.mImage.setImageBitmap(artical.getArticlePhoto());
            viewHoulder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ForumDetailActivity.actionStart((AppCompatActivity) context, artical.getObjectId());
                }
            });
        }else {
            // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
            ((FootHolder) viewHolder).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (mList.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((FootHolder) viewHolder).tips.setText("正在加载更多...");
                }
            } else {
                if (mList.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((FootHolder) viewHolder).tips.setText("没有更多数据了");

                    // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏提示条
                            ((FootHolder) viewHolder).tips.setVisibility(View.GONE);
                            // 将fadeTips设置true
                            fadeTips = true;
                            // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    // 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
    public int getRealLastPosition() {
        return mList.size();
    }

    // 暴露接口，改变fadeTips的方法
    public boolean isFadeTips() {
        return fadeTips;
    }

    // 暴露接口，下拉刷新时，通过暴露方法将数据源置为空
    public void resetDatas() {
        mList = new ArrayList<>();
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<Artical> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            mList.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    // 根据条目位置返回ViewType，以供onCreateViewHolder方法内获取不同的Holder
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
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
