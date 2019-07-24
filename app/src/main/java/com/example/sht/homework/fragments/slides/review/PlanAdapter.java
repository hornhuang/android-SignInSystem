package com.example.sht.homework.fragments.slides.review;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sht.homework.R;
import com.example.sht.homework.activities.WebActivity;
import com.example.sht.homework.baseclasses.Plan;
import com.example.sht.homework.utils.Dater;

import java.util.Date;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> mList;

    private Activity context;

    public PlanAdapter(List<Plan> mList, Activity context){
        this.mList   = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_plan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Plan plan = mList.get(i);
        viewHolder.mCourse.setText(plan.getCourse());
        viewHolder.mDate.setText(Dater.getYMDString(plan.getDate()));
        String str = "距离首次学习："+Dater.getDiscrepantDays(plan.getDate(), new Date())+"天";
        viewHolder.mDatePoor.setText(str);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.actionStart(context, plan.getUri());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private EditText mCourse;

        private TextView mDate;

        private TextView mDatePoor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCourse   = itemView.findViewById(R.id.course);
            mDate     = itemView.findViewById(R.id.date);
            mDatePoor = itemView.findViewById(R.id.date_poor);

//            mCourse.setInputType(InputType.TYPE_NULL);
        }
    }
}
