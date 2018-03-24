package com.example.charlie.test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by charlie on 3/8/18.
 */

public class Stocks  {
    //https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=demo
    private final String APIKEY = "6C5Y0OYIMP7F80B8";
    private String stockName;
    private double stockPrice;

    public Stocks(String stockName) {
        this.stockName = stockName.trim();
    }

    public String getStockName() {
        return stockName;
    }

    public String pushResultsToDB(){
        //will push the name of the stock as well as the price to the database once the database is completely built
        return this.stockName +": "+this.stockPrice;
    }

    public Double getStockPrice(String url) throws MalformedURLException, IOException, JSONException {

        URL reqURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+stockName+"&interval=1min&apikey="+APIKEY); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        request.setRequestMethod("GET");
        request.connect();
        //convertStreamToString(request.getInputStream())
        String in = convertStreamToString(request.getInputStream());
        JSONObject jsonObject = new JSONObject(in);

        String s = returnAPIcall(jsonObject);
        this.stockPrice = Double.parseDouble(s);

        return this.stockPrice;

    }

    public String returnAPIcall(JSONObject jsonObject) throws JSONException {
        String v = "";

        if (jsonObject.length() != 0){
            v = ((JSONObject)((JSONObject)jsonObject
                    .get(jsonObject
                            .names()
                            .getString(1)))
                    .get(((JSONObject)jsonObject
                            .get(jsonObject
                                    .names()
                                    .getString(1)))
                            .names()
                            .getString(0)))
                    .get(((JSONObject)((JSONObject)jsonObject
                            .get(jsonObject
                                    .names()
                                    .getString(1)))
                            .get(((JSONObject)jsonObject
                                    .get(jsonObject
                                            .names()
                                            .getString(1)))
                                    .names()
                                    .getString(0)))
                            .names()
                            .getString(3))
                    .toString();
        }else{
            v = "-1000000";
        }

        return v;


    }
    public static String convertStreamToString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
