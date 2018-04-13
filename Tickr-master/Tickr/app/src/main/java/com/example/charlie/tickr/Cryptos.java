package com.example.charlie.tickr;

import android.support.v7.app.AppCompatActivity;

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

public class Cryptos extends AppCompatActivity implements Runnable{
    public void setValue(double value) {
        this.value = value;
    }

    private volatile double value;
    private final String APIKEY = "6C5Y0OYIMP7F80B8";
    private String coinName;
    private double coinPrice;
    public Cryptos(String coinName) {
        this.coinName = coinName.trim();
    }

    public Cryptos() {

    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinName() {
        return coinName;
    }
    public String pushResultsToDB() {
        //will push the name of the coin as well as the price to the database once the database is completely built
        return this.coinName + ": " + this.coinPrice;
    }
    public Double getCoinPrice(String url) throws MalformedURLException, IOException, JSONException {
        URL reqURL = new URL("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_INTRADAY&symbol=" + this.coinName + "&market=EUR&apikey=" + APIKEY); //the URL we will send the request to
        HttpURLConnection request = (HttpURLConnection) (reqURL.openConnection());
        request.setRequestMethod("GET");
        request.connect();
        //convertStreamToString(request.getInputStream())
        String in = convertStreamToString(request.getInputStream());
        JSONObject jsonObject = new JSONObject(in);
        String s = returnAPIcall(jsonObject);
        this.coinPrice = Double.parseDouble(s);
        return this.coinPrice;
    }
    public String returnAPIcall(JSONObject jsonObject) throws JSONException {
        String v = "";
        if (jsonObject.length() != 0) {
            v = ((JSONObject) ((JSONObject) jsonObject
                    .get(jsonObject
                            .names()
                            .getString(1)))
                    .get(((JSONObject) jsonObject
                            .get(jsonObject
                                    .names()
                                    .getString(1)))
                            .names()
                            .getString(0)))
                    .get(((JSONObject) ((JSONObject) jsonObject
                            .get(jsonObject
                                    .names()
                                    .getString(1)))
                            .get(((JSONObject) jsonObject
                                    .get(jsonObject
                                            .names()
                                            .getString(1)))
                                    .names()
                                    .getString(0)))
                            .names()
                            .getString(1))
                    .toString();
        } else {
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

    @Override
    public void run() {
        try {
            value = getCoinPrice(this.coinName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getValue() {
        return value;
    }
}
