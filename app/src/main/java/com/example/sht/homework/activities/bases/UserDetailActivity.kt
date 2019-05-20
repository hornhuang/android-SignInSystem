package com.example.sht.homework.activities.bases

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.WindowManager
import com.example.sht.homework.R
import com.example.sht.homework.baseclasses.User
import com.example.sht.homework.utils.bmobmanager.SingleObject
import com.example.sht.homework.utils.bmobmanager.picture.FinalImageLoader
import com.example.sht.homework.utils.MyLog
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.activity_user_detail.*
import java.util.ArrayList

class UserDetailActivity : AppCompatActivity() {

    private var objectId:String = ""
    private var itemUser:User ?= null

    private var handler:Handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            iniUser()
            iniChart()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        objectId = intent.getStringExtra("objectId")
        SingleObject.getUser(this, objectId)
//        getUser()
        iniToolbar()
    }

    private fun iniToolbar(){
        this.setSupportActionBar(toolbar)
        val mToolbar = supportActionBar
        mToolbar!!.title = "空间"//这个在mainfest文件也可以直接定义
        mToolbar.setDisplayHomeAsUpEnabled(true)//设置返回按钮
        mToolbar.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                return false
            }
        }
        return false
    }

    companion object {

        fun anctionStart(activity:AppCompatActivity, objectId:String){
            var intent = Intent()
            intent.setClass(activity,UserDetailActivity::class.java)
            intent.putExtra("objectId", objectId)
            activity.startActivity(intent)
        }

    }

    private fun iniUser(){
        if (itemUser!!.imageFile == null){
            user_image.setImageResource(R.mipmap.app_icon)
        }else{
            FinalImageLoader(user_image, itemUser!!.imageFile).loadSmall()
        }
        user_name.text = itemUser!!.fullname
        user_motto.text = itemUser!!.motto
    }

    private fun iniChart() {
        refleshlayout.isRefreshing = true
        bc.setExtraOffsets(24f, 48f, 24f, 24f)
        setDescription("实验室个人自习时间统计表")
        setLegend()
        setYAxis()
        setXAxis()
        setChartData()
        bc.invalidate()
        refleshlayout.isRefreshing = false
    }

    private fun setDescription(descriptionStr: String) {
        val description = Description()
        description.text = descriptionStr
        description.textSize = 18f
        description.textAlign = Paint.Align.CENTER // 文本居中对齐
        // 计算描述位置
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        val paint = Paint()
        paint.textSize = 18f
        paint.typeface = Typeface.DEFAULT_BOLD
        val x = (outMetrics.widthPixels / 2).toFloat()
        val y = Utils.calcTextHeight(paint, descriptionStr) + Utils.convertDpToPixel(24f)
        description.setPosition(x, y)
        bc.description = description

    }

    private fun setLegend() {
        val legend = bc.legend
        legend.textSize = 14f
        legend.xOffset = 24f
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // 图例在水平线上向右对齐
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP // 图例在垂直线上向上对齐
        legend.orientation = Legend.LegendOrientation.VERTICAL // 图例条目垂直方向排列
        legend.setDrawInside(true) // 绘制在图表内部
    }

    private fun setYAxis() {
        // 左侧Y轴
        val axisLeft = bc.axisLeft
        axisLeft.axisMinimum = 0f // 最小值为0
        axisLeft.axisMaximum = 1440f // 最大值为1200
        val iAxisValueFormatter = IAxisValueFormatter { value, axis ->
            // 自定义值的格式
            value.toInt().toString() + ""
        }
        // 右侧Y轴
        bc.axisRight.isEnabled = false // 不启用
    }

    private fun setXAxis() {
        // X轴
        val xAxis = bc.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM // 位于底部
        xAxis.setDrawGridLines(false) // 不绘制X轴网格线
        xAxis.axisMinimum = 0f // 最小值-0.3f，为了使左侧留出点空间
        xAxis.granularity = 0.75f // 间隔尺寸1
        xAxis.textSize = 14f // 文本大小14
    }

    // 重置途中数据
    private fun setChartData() {
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(1f, itemUser!!.getmMondatTime().toFloat()))
        yVals1.add(BarEntry(2f, itemUser!!.getmTuesdayTime().toFloat()))
        yVals1.add(BarEntry(3f, itemUser!!.getmWednesdayTime().toFloat()))
        yVals1.add(BarEntry(4f, itemUser!!.getmThursdayTime().toFloat()))
        yVals1.add(BarEntry(5f, itemUser!!.getmFridayTime().toFloat()))
        yVals1.add(BarEntry(6f, itemUser!!.getmSaturdayTime().toFloat()))
        yVals1.add(BarEntry(7f, itemUser!!.getmSundayTime().toFloat()))

        val barDataSet1 = BarDataSet(yVals1, "自习时间")
        barDataSet1.valueTextColor = resources.getColor(R.color.colorPrimary)
        barDataSet1.color = resources.getColor(R.color.colorPrimary)
        barDataSet1.valueTextSize = 14f

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet1)

        val bardata = BarData(dataSets)
        bardata.barWidth = 0.5f

        bc.data = bardata
    }

    fun getHandler():Handler{
        return handler
    }

    fun setItemUser(currentUser:User){
        itemUser = currentUser
        MyLog.Log("111--"+currentUser!!.equals(null).toString())
        MyLog.Log("222--"+itemUser!!.equals(null).toString())
    }

}
