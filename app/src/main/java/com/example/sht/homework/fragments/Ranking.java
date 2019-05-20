package com.example.sht.homework.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.sht.homework.activities.MainActivity;
import com.example.sht.homework.R;
import com.example.sht.homework.baseclasses.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class Ranking extends Fragment {

    private User user;
    private BarChart bc;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        user = BmobUser.getCurrentUser(User.class);
        initView(view);
        iniRefleshFunction();
        initData();
        return view;
    }

    private void initView(View view) {
        bc = view.findViewById(R.id.bar_chart);
        refreshLayout = view.findViewById(R.id.chart_reflesh);
    }

    private void iniRefleshFunction(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initData() {
        refreshLayout.setRefreshing(true);
        bc.setExtraOffsets(24f,48f,24f,24f);
        setDescription("实验室个人自习时间统计表");
        setLegend();
        setYAxis();
        setXAxis();
        setChartData();
        refreshLayout.setRefreshing(false);
    }

    private void setDescription(String descriptionStr) {
        Description description = new Description();
        description.setText(descriptionStr);
        description.setTextSize(18f);
        description.setTextAlign(Paint.Align.CENTER); // 文本居中对齐
        // 计算描述位置
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        Paint paint = new Paint();
        paint.setTextSize(18f);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float x = outMetrics.widthPixels / 2;
        float y =  Utils.calcTextHeight(paint, descriptionStr) + Utils.convertDpToPixel(24);
        description.setPosition(x, y);
        bc.setDescription(description);

    }

    private void setLegend() {
        Legend legend = bc.getLegend();
        legend.setTextSize(14f);
        legend.setXOffset(24f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // 图例在水平线上向右对齐
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 图例在垂直线上向上对齐
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例条目垂直方向排列
        legend.setDrawInside(true); // 绘制在图表内部
    }

    private void setYAxis() {
        // 左侧Y轴
        YAxis axisLeft = bc.getAxisLeft();
        axisLeft.setAxisMinimum(0); // 最小值为0
        axisLeft.setAxisMaximum(1440); // 最大值为1200
        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() { // 自定义值的格式
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "";
            }
        };
        // 右侧Y轴
        bc.getAxisRight().setEnabled(false); // 不启用
    }

    private void setXAxis() {
        // X轴
        XAxis xAxis = bc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 位于底部
        xAxis.setDrawGridLines(false); // 不绘制X轴网格线
        xAxis.setAxisMinimum(0f); // 最小值-0.3f，为了使左侧留出点空间
        xAxis.setGranularity(0.75f); // 间隔尺寸1
        xAxis.setTextSize(14f); // 文本大小14
    }

    /*
    重置途中数据
     */
    private void setChartData() {
        final List<BarEntry> yVals1 = new ArrayList<>();
        yVals1.add(new BarEntry(1f, user.getmMondatTime())   );
        yVals1.add(new BarEntry(2f, user.getmTuesdayTime())  );
        yVals1.add(new BarEntry(3f, user.getmWednesdayTime()));
        yVals1.add(new BarEntry(4f, user.getmThursdayTime()) );
        yVals1.add(new BarEntry(5f, user.getmFridayTime())   );
        yVals1.add(new BarEntry(6f, user.getmSaturdayTime()) );
        yVals1.add(new BarEntry(7f, user.getmSundayTime())   );

        BarDataSet barDataSet1 = new BarDataSet(yVals1, "自习时间");
        barDataSet1.setValueTextColor(getResources().getColor(R.color.colorPrimary));
        barDataSet1.setColor(getResources().getColor(R.color.colorPrimary));
        barDataSet1.setValueTextSize(14f);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        BarData bardata = new BarData(dataSets);
        bardata.setBarWidth(0.5f);

        bc.setData(bardata);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        }else{  // 在最前端显示 相当于调用了onResume();
            //网络数据刷新
        }
    }
}
