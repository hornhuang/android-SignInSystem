package com.example.sht.homework.fragments.slides.review;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.Plan;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> mList;

    private Context context;

    private SimpleDateFormat format;

    public PlanAdapter(List<Plan> mList, Context context){
        this.mList   = mList;
        this.context = context;
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_plan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Plan plan = mList.get(i);
        viewHolder.mCourse.setText(plan.getCourse());
        viewHolder.mDate.setText(format.format(plan.getDate()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mCourse;

        private TextView mDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourse = itemView.findViewById(R.id.course);
            mDate   = itemView.findViewById(R.id.date);
        }
    }
}
