package com.example.joker.signinsystem.forums.adapters;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joker.signinsystem.R;
import com.example.joker.signinsystem.baseclasses.Artical;
import com.example.joker.signinsystem.bmobmanager.AvatarLoader;

import java.util.List;
import java.util.zip.Inflater;

public class ArticalAdapter extends RecyclerView.Adapter<ArticalAdapter.ViewHoulder> {

    private List<Artical> mList;

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
        return new ViewHoulder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoulder viewHoulder, int i) {
        Artical artical = mList.get(i);
        viewHoulder.mTitleText.setText(artical.getArticalTitleText());
        viewHoulder.mContentText.setText(artical.getArticalContextText());
        new AvatarLoader(viewHoulder.mImage, artical.getArticalImageFile()).articalload();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
