package com.ori.origami;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.origami.origami.base.act.AnnotationActivity;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.annotation.BView;
import com.origami.utils.Dp2px;
import com.origami.utils.StatusUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @by: origami
 * @date: {2021-09-13}
 * @info:
 **/
@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_recycler_test)
public class Test_recycler extends AnnotationActivity {

    final List<String> dates = new ArrayList<>();

    @BView(R.id.test)
    LineChartView lineChartView;

    private Timer timer;
//    @BView(R.id.test_recycler)
//    RecyclerView recyclerView;

//    final Adapter adapter = new Adapter();

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//        for (int i = 0; i < 30; i++) {
//            dates.add(String.valueOf(i));
//        }
//        adapter.notifyDataSetChanged();
//        recyclerView.post(()-> setOverScroller(recyclerView, Dp2px.dp2px(300), Dp2px.dp2px(300)));
        init2();
    }

    List<PointValue> pValues = new LinkedList<>();
    List<AxisValue> yAxisValues = new LinkedList<>();
    List<AxisValue> xAxisValues = new LinkedList<>();

    private void init2() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                add(new Random().nextInt(100));
            }
        }, 3000, 2000);
    }

    LineChartData chartData = new LineChartData();
    int i = 0;
    int show_max = 7;
    int max = 10;
    private void add(int val){
        i++;
        Log.e("ORI", i + "---" + val);
        pValues.add(new PointValue(i, val));
        if(pValues.size() > 30){ pValues.remove(0); }
//        initX();
        initY();
        initLines();
        lineChartView.setLineChartData(chartData);
        Viewport port;
        if (i > show_max) {
            port = initViewPort(i - show_max, i + max - show_max);
        } else {
            port = initViewPort(1, max);
        }
        lineChartView.setMaximumViewport(port);
        lineChartView.setCurrentViewport(port);
    }

    private void initLines(){
        Line line = new Line(pValues);
        line.setStrokeWidth(2);
        line.setFilled(true);
        line.setColor(getResources().getColor(R.color._ori_yellow));//设置折线颜色
        line.setShape(ValueShape.CIRCLE);//设置折线图上数据点形状为 圆形 （共有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，true是平滑曲线，false是折线
        line.setHasLabels(true);//数据是否有标注
//        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据,设置了line.setHasLabels(true);之后点击无效
        line.setHasLines(true);//是否用线显示，如果为false则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）
        chartData.setBaseValue(40);
        List<Line> lines = chartData.getLines();
        if(lines == null){
            chartData.setLines(new ArrayList<Line>(){ {add(line);} });
        }else {
            lines.clear();
            lines.add(line);
        }
    }

    private void initY() {
        Axis yLeft = chartData.getAxisYLeft();
        if(yLeft == null) {
            Axis axisY = new Axis();  //Y轴
            axisY.setTextColor(getResources().getColor(R.color._ori_red));
            axisY.setValues(new ArrayList<AxisValue>(){{
//                add(new AxisValue(0));
                add(new AxisValue(40));
                add(new AxisValue(100));
            }});
//            axisY.setName("能量值");
            axisY.setHasSeparationLine(true);
            axisY.setLineColor(getResources().getColor(R.color._ori_red));
            axisY.setHasLines(true);
            axisY.setMaxLabelChars(3); //默认是3，只能看最后三个数字
            chartData.setAxisYLeft(axisY);
        }
    }

    private void initX(){
        xAxisValues.add(new AxisValue(i).setLabel("" + i));
        if(xAxisValues.size() > 10){ xAxisValues.remove(0); }
        Axis xBottom = chartData.getAxisXBottom();
        if(xBottom == null){
            Axis axisX = new Axis(); //X轴
            axisX.setHasTiltedLabels(true);
            axisX.setTextColor(Color.BLACK);
            axisX.setMaxLabelChars(2);
            axisX.setHasLines(true);
            axisX.setValues(xAxisValues);
            chartData.setAxisXBottom(axisX);
        }else {
            xBottom.setValues(xAxisValues);
        }
    }

    private Viewport initViewPort(float left,float right) {
        Viewport port = new Viewport();
        port.top = 100;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        port.left = left;//X轴左边界，变化
        port.right = right;//X轴右边界，变化
        return port;
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusUtils.setLightStatusBar(this, true);
        View viewById = getWindow().getDecorView().findViewById(android.R.id.content);
        viewById.setBackgroundColor(getResources().getColor(R.color._ori_gray));
        viewById.setPadding(0, StatusUtils.getStatusBarHeight(this), 0, 0);
    }


    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dp2px.dp2px(60));
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(Test_recycler.this.getResources().getColor(R.color._ori_white));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            return new RecyclerView.ViewHolder(textView) { };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(dates.get(position));
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }
    }


    /**
     * 确实替换了，也调用到了打印了输出，但是没效果，其他地方应该还有存在限制
     * @param recyclerView
     * @param overX
     * @param overY
     */
    public static void setOverScroller(RecyclerView recyclerView, final int overX, final int overY){
        try {
            Class<?> aClass = recyclerView.getClass();
            Field mViewFlinger = aClass.getDeclaredField("mViewFlinger");
            mViewFlinger.setAccessible(true);
            Object o =mViewFlinger.get(recyclerView);
            Field mScroller = o.getClass().getDeclaredField("mOverScroller");
            mScroller.setAccessible(true);
            Field sQuinticInterpolator = o.getClass().getDeclaredField("mInterpolator");
            sQuinticInterpolator.setAccessible(true);
            mScroller.set(o, new OverScroller(recyclerView.getContext(), (Interpolator) sQuinticInterpolator.get(o)){
                @Override
                public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
                    Log.e("ORI","惯性滑动被我反射修改啦");
                    this.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
                }
            });
            Log.e("ORI","替换完成");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
