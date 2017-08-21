package com.db.weather.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.db.R;
import com.db.util.ProgressUtil;
import com.db.weather.adapter.WeatherListAdapter;
import com.db.weather.mvp.model.bean.DailyWeatherListBean;
import com.db.weather.mvp.model.bean.FutureWeatherBean;
import com.db.weather.mvp.presenter.impl.WeatherListPresenterImpl;
import com.db.weather.mvp.view.IWeatherListView;
import com.db.widget.fragment.BaseFragment;
import com.db.widget.hellocharts.gesture.ContainerScrollType;
import com.db.widget.hellocharts.gesture.ZoomType;
import com.db.widget.hellocharts.model.Axis;
import com.db.widget.hellocharts.model.AxisValue;
import com.db.widget.hellocharts.model.Line;
import com.db.widget.hellocharts.model.LineChartData;
import com.db.widget.hellocharts.model.PointValue;
import com.db.widget.hellocharts.model.ValueShape;
import com.db.widget.hellocharts.model.Viewport;
import com.db.widget.hellocharts.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class WeatherListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        IWeatherListView, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String KEY = "key";

    private WeatherListPresenterImpl weatherListPresenter;

    LineChartView chart;

    RecyclerView recyclerView;

    LinearLayout ll_weather;

    private WeatherListAdapter listAdapter;

    private Context context;

    //天气预报列表选中子项的位置
    private int selectedItemIndex = 0;

    private List<PointValue> mWeightValues = new ArrayList<PointValue>();//体重的坐标集合
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();//时间的坐标标注集合

    String[] date = {"08.06.2016", "09.06.2016", "10.06.2016", "11.06.2016",
            "12.06.2016", "13.06.2016", "14.06.2016", "15.06.2016", "16.06.2016",
            "17.06.2016", "18.06.2016", "19.06.2016", "20.06.2016", "21.06.2016",
            "22.06.2016", "23.06.2016", "24.06.2016", "25.06.2016", "26.06.2016",
            "27.06.2016", "28.06.2016", "29.06.2016", "30.06.2016", "31.06.2016"};//X轴的底部时间标注集合

    float[] weather = {125.8f, 124.8f, 126.1f, 127.2f, 126.5f, 126.9f, 125.9f, 126.0f,
            125.8f, 124.8f, 126.1f, 127.2f, 126.5f, 126.9f, 125.9f, 126.0f,
            125.8f, 124.8f, 126.1f, 127.2f, 126.5f, 126.9f, 125.9f, 126.0f};//图表体重数据集合

    public static WeatherListFragment newInstance() {
        return newInstance("");
    }

    private static WeatherListFragment newInstance(String game_type) {
        WeatherListFragment fragment = new WeatherListFragment();
        Bundle args = new Bundle();
        args.putString(KEY, game_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ll_weather = (LinearLayout) view.findViewById(R.id.ll_weather);
        chart = (LineChartView) view.findViewById(R.id.line_chart_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        context = view.getContext();

        initView();

        return attachToSwipeBack(view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        weatherListPresenter.getDailyWeather("36bdd59658111bc23ff2bf9aaf6e345c", "zh-chs", "CHGD000000");
        weatherListPresenter.getFutureWeather("CHGD000000");
    }

    private void initView() {
        //初始化MVP
        weatherListPresenter = new WeatherListPresenterImpl(this);
        //设置RefreshLayout
        //设置RecyclerView
        listAdapter = new WeatherListAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(listAdapter);

        recyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectedItemIndex = position;
                listAdapter.setSelectedIndex(selectedItemIndex);
                listAdapter.notifyDataSetChanged();
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position - 2, 0);

            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(DailyWeatherListBean bean) {
        for (int i = 0; i < date.length; i++) {
            mWeightValues.add(new PointValue(i, weather[i], bean.getHourly().get(i).getText(), bean.getHourly().get(i).getCode()));//体重坐标数据
        }
    }

    private void initLineChart() {    //折线的集合，也就是体重和脂肪的折线保存的集合
        List<Line> lines = new ArrayList<Line>();    //体重的折线
        Line line = new Line(mWeightValues).setColor(Color.WHITE);  //折线的颜色和坐标数据
        //折线图上每个数据点的形状（有三种 ：ValueShape.SQUARE正方形  ValueShape.CIRCLE圆点  ValueShape.DIAMOND菱形）
        line.setShape(ValueShape.CIRCLE);
        line.setStrokeWidth(2);
        line.setPointRadius(2);
        line.setCubic(true);//曲线是否平滑
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注//  line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);//将体重折线保存到集合中

        // LineChartData是宏观上面的折线数据显示，
        //因为我们已经将所有的数据都填充进折线中，
        //现在只需要将它保存到LineChartData中
        LineChartData data = new LineChartData(lines);    //设置时间坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setTextSize(8);//设置字体大小
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        axisX.setHasLines(true);
        axisX.setLineColor(ContextCompat.getColor(getContext(), R.color.line_chart_renderer_line_color));
        data.setAxisXBottom(axisX); //x 轴在底部

        //设置Y坐标轴
        Axis axisY = new Axis();  //Y轴
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        //将所有的数据填充到折线控件中
        chart.setLineChartData(data);    //设置行为属性，支持缩放、滑动以及平移
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setVisibility(View.VISIBLE);    //设置一下整体的Y轴显示的开始和结束坐标
        final Viewport v1 = new Viewport(chart.getMaximumViewport());
        v1.bottom = 20;
        v1.top = 45;    // You have to set max and current viewports separately.
        chart.setMaximumViewport(v1);
        //设置当前的窗口显示多少个坐标数据，必须将折线的可以缩放的开关打开
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        v.right = 6;
        chart.setCurrentViewport(v);
    }

    @Override
    public void updateDailyWeather(DailyWeatherListBean bean) {
        date = new String[24];
        for (int i = 0; i < bean.getHourly().size(); i++) {
            date[i] = bean.getHourly().get(i).getTime().substring(11, 16);
        }

        weather = new float[bean.getHourly().size()];
        for (int i = 0; i < bean.getHourly().size(); i++) {
            weather[i] = Float.parseFloat(bean.getHourly().get(i).getTemperature());
        }

        getAxisXLables();//获取x轴的标注
        getAxisPoints(bean);//获取坐标点
        initLineChart();//初始化
    }

    @Override
    public void updateFutureWeather(FutureWeatherBean bean) {
        listAdapter.setNewData(bean.getWeather().get(0).getFuture());
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @OnClick({R.id.toolbar_iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_iv_back:
                pop();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showError(String message) {
        ProgressUtil.dismiss();
        ToastUtils.showShort(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
