package com.db.weather.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.db.R;
import com.db.weather.WeatherImageUtil;
import com.db.weather.mvp.model.bean.FutureWeatherBean;
import com.db.widget.CurveView;

import java.util.List;

public class WeatherListAdapter extends BaseQuickAdapter<FutureWeatherBean.WeatherBean.FutureBean, BaseViewHolder> {

    private CurveView curveView;

    private int index = 0;

    public WeatherListAdapter(List<FutureWeatherBean.WeatherBean.FutureBean> data) {
        super(R.layout.item_weather, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, FutureWeatherBean.WeatherBean.FutureBean bean) {

        String[] text = bean.getText().split("/");
        if (text.length < 2)
            text[1] = text[0];

        viewHolder.setText(R.id.tv_date, bean.getDay())
                .setText(R.id.tv_date_2, bean.getDate())
                .setText(R.id.tv_weather_src_low, text[1])
                .setText(R.id.tv_weather_src_height, text[0]);

        if (viewHolder.getAdapterPosition() == index)
            viewHolder.setBackgroundColor(R.id.ll_weather_item,
                    mContext.getResources().getColor(R.color.color_white_3));
        else
            viewHolder.setBackgroundColor(R.id.ll_weather_item,
                    mContext.getResources().getColor(R.color.transparent));

        Glide.with(mContext)
                .load(WeatherImageUtil.getResourceId(bean.getCode1()))
                .centerCrop()
                .crossFade()
                .into((ImageView) viewHolder.getView(R.id.iv_weather_height));

        Glide.with(mContext)
                .load(WeatherImageUtil.getResourceId(bean.getCode2()))
                .centerCrop()
                .crossFade()
                .into((ImageView) viewHolder.getView(R.id.iv_weather_low));

        curveView = viewHolder.getView(R.id.cv_weather);
        curveView.setIndex(viewHolder.getAdapterPosition());
        curveView.setData(getData());
    }

    public void setSelectedIndex(int index) {
        this.index = index;
    }
}
