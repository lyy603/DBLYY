package com.db.widget.hellocharts.formatter;

import com.db.widget.hellocharts.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
