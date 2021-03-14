package com.example.stockmarketanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class stockActivity extends AppCompatActivity {

    List<StockMarketDate> stockMarketDates = new ArrayList<>();
    List<String> stockMarketDatesStrings = new ArrayList<>();
    int indexOfStart, indexOfEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ListView listView = (ListView) findViewById(R.id.dataList);
        listView.setVisibility(View.GONE);
        EditText editText = (EditText) findViewById(R.id.trendTextView);
        editText.setVisibility(View.GONE);
        Button b1 = (Button) findViewById(R.id.backButton);
        b1.setVisibility(View.GONE);

        Intent intent = getIntent();
        stockMarketDates = (List<StockMarketDate>) intent.getSerializableExtra("STOCK_DATA_LIST");
        indexOfStart = intent.getIntExtra("START_INDEX", 1);
        indexOfEnd = intent.getIntExtra("END_INDEX", 0);

    }

    public void onGetTrend(View view) {
        EditText editText = (EditText) findViewById(R.id.trendTextView);
        getUpwardTrend(stockMarketDates, indexOfStart, indexOfEnd);

        Button b1 = (Button) findViewById(R.id.trendButton);
        b1.setVisibility(View.GONE);
        Button b2 = (Button) findViewById(R.id.volumeButton);
        b2.setVisibility(View.GONE);
        Button b3 = (Button) findViewById(R.id.smaButton);
        b3.setVisibility(View.GONE);
        Button b4 = (Button) findViewById(R.id.prevActivityButton);
        b4.setVisibility(View.GONE);
        ListView listView = (ListView) findViewById(R.id.dataList);
        listView.setVisibility(View.GONE);

        Button b5 = (Button) findViewById(R.id.backButton);
        b5.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
    }

    public void onGetVolumePriceChange(View view) {
        getHighestVolumePriceChange(stockMarketDates, indexOfStart, indexOfEnd);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stockMarketDatesStrings);
        ListView listView = (ListView) findViewById(R.id.dataList);
        listView.setAdapter(adapter);

        Button b1 = (Button) findViewById(R.id.trendButton);
        b1.setVisibility(View.GONE);
        Button b2 = (Button) findViewById(R.id.volumeButton);
        b2.setVisibility(View.GONE);
        Button b3 = (Button) findViewById(R.id.smaButton);
        b3.setVisibility(View.GONE);
        Button b4 = (Button) findViewById(R.id.prevActivityButton);
        b4.setVisibility(View.GONE);
        EditText editText = (EditText) findViewById(R.id.trendTextView);
        editText.setVisibility(View.GONE);

        Button b5 = (Button) findViewById(R.id.backButton);
        b5.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    public void onGetSma5(View view) {
        getSMA(stockMarketDates, indexOfStart, indexOfEnd);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stockMarketDatesStrings);
        ListView listView = (ListView) findViewById(R.id.dataList);
        listView.setAdapter(adapter);

        Button b1 = (Button) findViewById(R.id.trendButton);
        b1.setVisibility(View.GONE);
        Button b2 = (Button) findViewById(R.id.volumeButton);
        b2.setVisibility(View.GONE);
        Button b3 = (Button) findViewById(R.id.smaButton);
        b3.setVisibility(View.GONE);
        Button b4 = (Button) findViewById(R.id.prevActivityButton);
        b4.setVisibility(View.GONE);
        EditText editText = (EditText) findViewById(R.id.trendTextView);
        editText.setVisibility(View.GONE);

        Button b5 = (Button) findViewById(R.id.backButton);
        b5.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    public void backToPrevActivity(View view) {
        Intent intent = new Intent( this, MainActivity.class );
        startActivity( intent );
    }

    public void backToSelection(View view) {
        Button b1 = (Button) findViewById(R.id.trendButton);
        b1.setVisibility(View.VISIBLE);
        Button b2 = (Button) findViewById(R.id.volumeButton);
        b2.setVisibility(View.VISIBLE);
        Button b3 = (Button) findViewById(R.id.smaButton);
        b3.setVisibility(View.VISIBLE);
        Button b4 = (Button) findViewById(R.id.prevActivityButton);
        b4.setVisibility(View.VISIBLE);

        EditText editText = (EditText) findViewById(R.id.trendTextView);
        editText.setVisibility(View.GONE);
        Button b5 = (Button) findViewById(R.id.backButton);
        b5.setVisibility(View.GONE);
        ListView listView = (ListView) findViewById(R.id.dataList);
        listView.setVisibility(View.GONE);
    }




    // Function to calculate SMA 5 for the given dates
    private void getSMA(List<StockMarketDate> stockMarketDates, int indexOfStart, int indexOfEnd) {
        stockMarketDatesStrings.clear();

        int maxIndex = stockMarketDates.size() - 6;
        // If the start date is at the end of the list,
        // and there are no data from 5 previous dates, then the SMA 5 cannot be calculated.
        // these dates are ignored, and the starting index is set to a date from which the SMA 5 can be calculated.
        if(indexOfStart > maxIndex) {
            for(int i = indexOfStart; i < stockMarketDates.size(); i++) {
                stockMarketDates.get(i).setPercentageOfSMA5(0);
            }
            while(indexOfStart > maxIndex) {
                indexOfStart--;
            }
        }

        for(int i = indexOfStart; i >= indexOfEnd; i--) {
            double smaSum = 0, smaAverage = 0, percentage = 0;

            // List indexes decrease to the future, and increase to the past
            // So the previous day from the current day is i + 1, and not i - 1;
            for(int j = i + 1; j <= i + 5; j++) {
                smaSum += stockMarketDates.get(j).getClose(); // add the closing prices together from past 5 days
            }
            smaAverage = smaSum / 5; // get average price

            percentage = (1 - (smaAverage / stockMarketDates.get(i).getOpen())) * 100; // calculate percentage of the current day's opening price.


            stockMarketDates.get(i).setPercentageOfSMA5(percentage); // set the percentages for each date
        }

        // create a new list which will be sorted by price change percentages
        List<StockMarketDate> selectedDates = new ArrayList<>();
        for(int i = indexOfStart; i >= indexOfEnd; i--) {
            selectedDates.add(stockMarketDates.get(i));
        }
        //sort the list
        selectedDates.sort((StockMarketDate smd1, StockMarketDate smd2) -> Double.compare(smd1.getPercentageOfSMA5(), smd2.getPercentageOfSMA5()));

        NumberFormat df = new DecimalFormat("+0.00;-0.00"); // formatting the values for a nicer output.

        for(int i = selectedDates.size() - 1; i >= 0; i--) {
            stockMarketDatesStrings.add(selectedDates.get(i).getDate() + ", " + df.format(selectedDates.get(i).getPercentageOfSMA5()) + "%");
        }



    }

    //Function to find the longest upward trend within the date range
    private void getUpwardTrend(List<StockMarketDate> stockMarketDates, int indexOfStart, int indexOfEnd) {

        int longestUpwardTrend = 0;
        String startDate = "", endDate = "";
        while(indexOfStart > indexOfEnd) {
            int i = indexOfStart, trend = 0;
            String start = stockMarketDates.get(i).getDate(); // save the current date from which the trend calculation will start

            while(i > indexOfEnd) { // calculate upward trends from each day

                //store the trends in a temporary variable
                if(stockMarketDates.get(i - 1).getClose() > stockMarketDates.get(i).getClose()) {
                    trend += 1;
                    i--;
                }
                else break;
            }
            String end = stockMarketDates.get(i).getDate(); // save the end date to which the trend calculation ends.

            // if the calculated trend is longer than the previously saved longest trend, replace the longest trend value and the start and end dates.
            if(trend > longestUpwardTrend) {
                longestUpwardTrend = trend;
                startDate = start;
                endDate = end;
            }

            indexOfStart--;
        }

        String trendText = "In stock historical data the Close/Last price increased " + longestUpwardTrend + " days in a row, between " + startDate + " and " + endDate + ".";
        EditText editText = (EditText) findViewById(R.id.trendTextView);
        editText.setText(trendText);

    }

    //Function to get the highest trading volumes and price changes from the selected date range.
    private void getHighestVolumePriceChange(List<StockMarketDate> stockMarketDates, int indexOfStart, int indexOfEnd) {
        stockMarketDatesStrings.clear();
        DecimalFormat numberFormat = new DecimalFormat("0.00");

        // create a new list which will be sorted according to trading volumes.
        List<StockMarketDate> selectedDates = new ArrayList<>();
        for(int i = indexOfStart; i >= indexOfEnd; i--) {
            selectedDates.add(stockMarketDates.get(i));
        }
        // sort the list
        selectedDates.sort((StockMarketDate smd1, StockMarketDate smd2) -> smd1.getVolume() - smd2.getVolume());

        for(int i = selectedDates.size() - 1; i > 0; i--) {
            stockMarketDatesStrings.add(selectedDates.get(i).getDate() + ", " + selectedDates.get(i).getVolume() + ", " + "$" + numberFormat.format(selectedDates.get(i).getPriceChange()));
        }

    }



}