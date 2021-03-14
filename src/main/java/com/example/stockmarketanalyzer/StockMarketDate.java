package com.example.stockmarketanalyzer;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DecimalFormat;

public class StockMarketDate implements Serializable {
    private String date;
    private double close;
    private int volume;
    private double open;
    private double high;
    private double low;
    private double priceChange;
    private double percentageOfSMA5;

    public StockMarketDate() {
        date = "01/01/1000";
        close = 0.00;
        volume = 0;
        open = 0.00;
        high = 0.00;
        low = 0.00;
        priceChange = 0.00;
        percentageOfSMA5 = 0.00;
    }
    public StockMarketDate(String date, double close, int volume, double open, double high, double low) {
        this.date = date;
        this.close = close;
        this.volume = volume;
        this.open = open;
        this.high = high;
        this.low = low;
        this.priceChange = high - low;
    }
    public String getDate() {
        return date;
    }
    public double getClose() {
        return close;
    }
    public int getVolume() {
        return volume;
    }
    public double getOpen() {
        return open;
    }
    public double getHigh() {
        return high;
    }
    public double getLow() {
        return low;
    }
    public double getPriceChange() {
        return priceChange;
    }
    public double getPercentageOfSMA5() {
        return percentageOfSMA5;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
    public void setOpen(double open) {
        this.open = open;
    }
    public void setHigh(double high) {
        this.high = high;
    }
    public void setLow(double low) {
        this.low = low;
    }
    public void setPercentageOfSMA5(double percentageOfSMA5) {
        this.percentageOfSMA5 = percentageOfSMA5;
    }

    public void printData() {
        System.out.println(date + ", " + close + ", " + volume + ", " + open + ", " + high + ", " + low);
    }
    public void printVolumePriceChange() {
        DecimalFormat numberFormat = new DecimalFormat("0.00");
        System.out.println(date + ", " + volume + ", " + "$" + numberFormat.format(priceChange));
    }

}
