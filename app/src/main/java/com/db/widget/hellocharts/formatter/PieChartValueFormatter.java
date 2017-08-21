package com.db.widget.hellocharts.formatter;

import com.db.widget.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
