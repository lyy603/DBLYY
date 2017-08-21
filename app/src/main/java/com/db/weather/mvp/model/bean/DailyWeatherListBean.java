package com.db.weather.mvp.model.bean;


import java.util.List;

public class DailyWeatherListBean {

    /**
     * status : OK
     * hourly : [{"text":"多云","code":"4","temperature":"16","time":"2017-02-19T13:00:00+08:00"},{"text":"多云","code":"4","temperature":"18","time":"2017-02-19T14:00:00+08:00"},{"text":"多云","code":"4","temperature":"17","time":"2017-02-19T15:00:00+08:00"},{"text":"多云","code":"4","temperature":"16","time":"2017-02-19T16:00:00+08:00"},{"text":"多云","code":"4","temperature":"16","time":"2017-02-19T17:00:00+08:00"},{"text":"多云","code":"4","temperature":"16","time":"2017-02-19T18:00:00+08:00"},{"text":"多云","code":"4","temperature":"15","time":"2017-02-19T19:00:00+08:00"},{"text":"多云","code":"4","temperature":"15","time":"2017-02-19T20:00:00+08:00"},{"text":"多云","code":"4","temperature":"15","time":"2017-02-19T21:00:00+08:00"},{"text":"多云","code":"4","temperature":"14","time":"2017-02-19T22:00:00+08:00"},{"text":"多云","code":"4","temperature":"14","time":"2017-02-19T23:00:00+08:00"},{"text":"多云","code":"4","temperature":"14","time":"2017-02-20T00:00:00+08:00"},{"text":"多云","code":"4","temperature":"15","time":"2017-02-20T01:00:00+08:00"},{"text":"小雨","code":"13","temperature":"15","time":"2017-02-20T02:00:00+08:00"},{"text":"小雨","code":"13","temperature":"15","time":"2017-02-20T03:00:00+08:00"},{"text":"小雨","code":"13","temperature":"15","time":"2017-02-20T04:00:00+08:00"},{"text":"中雨","code":"14","temperature":"15","time":"2017-02-20T05:00:00+08:00"},{"text":"中雨","code":"14","temperature":"13","time":"2017-02-20T06:00:00+08:00"},{"text":"中雨","code":"14","temperature":"10","time":"2017-02-20T07:00:00+08:00"},{"text":"小雨","code":"13","temperature":"8","time":"2017-02-20T08:00:00+08:00"},{"text":"小雨","code":"13","temperature":"6","time":"2017-02-20T09:00:00+08:00"},{"text":"小雨","code":"13","temperature":"5","time":"2017-02-20T10:00:00+08:00"},{"text":"小雨","code":"13","temperature":"5","time":"2017-02-20T11:00:00+08:00"},{"text":"小雨","code":"13","temperature":"6","time":"2017-02-20T12:00:00+08:00"}]
     */

    private String status;
    /**
     * text : 多云
     * code : 4
     * temperature : 16
     * time : 2017-02-19T13:00:00+08:00
     */

    private List<HourlyBean> hourly;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HourlyBean> getHourly() {
        return hourly;
    }

    public void setHourly(List<HourlyBean> hourly) {
        this.hourly = hourly;
    }

    public static class HourlyBean {
        private String text;
        private int code;
        private String temperature;
        private String time;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
