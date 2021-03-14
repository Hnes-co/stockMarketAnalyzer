package com.example.stockmarketanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String stock, startDate, endDate;
    private int indexOfStart, indexOfEnd;
    private Boolean validDates = false;
    private Boolean validStart = false, validEnd = false;
    private RequestQueue requestQueue;

    List<StockMarketDate> stockMarketDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null) {
            stock = savedInstanceState.getString("STOCK");
            startDate = savedInstanceState.getString("START_DATE");
            startDate = savedInstanceState.getString("END_DATE");
        }
        requestQueue = Volley.newRequestQueue(this);

        TextView loadingText = (TextView) findViewById(R.id.loadTextView);
        loadingText.setVisibility(View.GONE);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("STOCK", stock);
        outState.putString("START_DATE", startDate);
        outState.putString("END_DATE", endDate);

        super.onSaveInstanceState(outState);
    }

    public void analyzeStock(View view) {
        EditText editStockText = (EditText) findViewById(R.id.stockInput);
        stock = editStockText.getText().toString().toUpperCase();
        String urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stock + "&apikey=FBLVCVOY0XALLAIU";

        readStockData(urlString);
    }




    // function to read the stock data from API.
    private void readStockData(String urlString) {
        stockMarketDates.clear();
        toggleVisibility();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                response -> {
                    try {
                        JSONObject data = new JSONObject(response);
                        if(data.has("Error Message")){
                            Toast.makeText(this, "Invalid stock input", Toast.LENGTH_LONG).show();
                            toggleVisibility();
                        }
                        else {
                            JSONObject stockData = data.getJSONObject("Time Series (Daily)");
                            JSONArray stockKeys = stockData.names();

                            for(int i = 0; i < stockKeys.length(); i++){
                                assert stockKeys != null;
                                String date = stockKeys.getString(i);
                                JSONObject value = stockData.getJSONObject(date);
                                double open = Double.parseDouble(value.optString("1. open"));
                                double high = Double.parseDouble(value.optString("2. high"));
                                double low = Double.parseDouble(value.optString("3. low"));
                                double close = Double.parseDouble(value.optString("4. close"));
                                int volume = Integer.parseInt(value.optString("5. volume"));
                                StockMarketDate smd = new StockMarketDate(date, close, volume, open, high, low);
                                stockMarketDates.add(smd);
                            }
                            checkInput();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
                }
                );
        if(requestQueue != null) {
            requestQueue.add(stringRequest);
        }
    }

    public void checkInput() {
        EditText editFromDate = (EditText) findViewById(R.id.dateFromInput);
        startDate = editFromDate.getText().toString();
        EditText editToDate = (EditText) findViewById(R.id.dateToInput);
        endDate = editToDate.getText().toString();

        for(int i = 0; i < stockMarketDates.size(); i++) {
            if(stockMarketDates.get(i).getDate().equals(startDate)) {
                validStart = true;
                indexOfStart = i;
            }
            if(stockMarketDates.get(i).getDate().equals(endDate)) {
                validEnd = true;
                indexOfEnd = i;
            }
        }

        // check that the input is correct
        if(indexOfStart > indexOfEnd && validStart && validEnd) {
            validDates = true;
        }
        else {
            toggleVisibility();
            Toast.makeText(this, "Invalid date range input. Date must be given as 'year-month-day'. Start date time must be before end date time and weekend dates are invalid.", Toast.LENGTH_LONG).show();
        }

        if(validDates) {

            Intent openStockActivity = new Intent(this, stockActivity.class);
            openStockActivity.putExtra("STOCK_DATA_LIST", (Serializable) stockMarketDates);
            openStockActivity.putExtra("START_INDEX", indexOfStart);
            openStockActivity.putExtra("END_INDEX", indexOfEnd);
            startActivity(openStockActivity);
        }
    }


    public void toggleVisibility() {
        TextView loadingText = (TextView) findViewById(R.id.loadTextView);

        if(loadingText.getVisibility() == View.GONE) {
            TextView t1 = (TextView) findViewById(R.id.textView);
            t1.setVisibility(View.GONE);
            TextView stockInput = (TextView) findViewById(R.id.stockInput);
            stockInput.setVisibility(View.GONE);
            TextView dateTo = (TextView) findViewById(R.id.dateToInput);
            dateTo.setVisibility(View.GONE);
            TextView dateFrom = (TextView) findViewById(R.id.dateFromInput);
            dateFrom.setVisibility(View.GONE);
            TextView t3 = (TextView) findViewById(R.id.textView6);
            t3.setVisibility(View.GONE);
            Button analyze = (Button) findViewById(R.id.analyzeButton);
            analyze.setVisibility(View.GONE);

            loadingText.setVisibility(View.VISIBLE);
        }
        else {
            TextView t1 = (TextView) findViewById(R.id.textView);
            t1.setVisibility(View.VISIBLE);
            TextView stockInput = (TextView) findViewById(R.id.stockInput);
            stockInput.setVisibility(View.VISIBLE);
            TextView dateTo = (TextView) findViewById(R.id.dateToInput);
            dateTo.setVisibility(View.VISIBLE);
            TextView dateFrom = (TextView) findViewById(R.id.dateFromInput);
            dateFrom.setVisibility(View.VISIBLE);
            TextView t3 = (TextView) findViewById(R.id.textView6);
            t3.setVisibility(View.VISIBLE);
            Button analyze = (Button) findViewById(R.id.analyzeButton);
            analyze.setVisibility(View.VISIBLE);

            loadingText.setVisibility(View.GONE);
        }
    }

}