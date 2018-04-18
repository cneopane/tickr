package com.example.charlie.tickr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Stock extends AppCompatActivity {
String stockName="";
    String price, amount, timeStamp, total;
    String dateAsString, totalAsString;
    String stockName1 = "MSFT", stockName2 = "AAPL", stockName3 = "GOOG";

    Boolean fullArray =false;

    ArrayList<Date> dates1 = new ArrayList<Date>();
    ArrayList<Date> dates2 = new ArrayList<Date>();
    ArrayList<Date> dates3 = new ArrayList<Date>();
    ArrayList<Float> totals1 = new ArrayList<Float>();
    ArrayList<Float> totals2 = new ArrayList<Float>();
    ArrayList<Float> totals3 = new ArrayList<Float>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        getSupportActionBar().setTitle("Stock Value");

        Spinner spinner = (Spinner) findViewById(R.id.DropDown);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Stock.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.DropDown));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner.setAdapter(myAdapter);

spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                stockName = "AAPL";
                returnPriceOfSelectedStock(stockName);
                break;
            case 1:
                stockName = "MSFT";
                returnPriceOfSelectedStock(stockName);
                break;
            case 2:
                stockName = "AMZN";
                returnPriceOfSelectedStock(stockName);
                break;
            case 3:
                stockName = "DVMT";
                returnPriceOfSelectedStock(stockName);
                break;
            case 4:
                stockName = "DAL";
                returnPriceOfSelectedStock(stockName);
                break;
            case 5:
                stockName = "GOOG";
                returnPriceOfSelectedStock(stockName);
                break;
            case 6:
                stockName = "IBM";
                returnPriceOfSelectedStock(stockName);
                break;


        }


    }

    public String returnPriceOfSelectedStock(String stockName){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Stocks s = new Stocks(stockName);

        new Thread(s).start();
        try {
            //System.out.printf(s.returnAPIresponse("hello"));
            double value = s.getStockPrice(stockName);
            Calendar calendar = Calendar.getInstance();
            timeStamp = calendar.getTime()+"";
            price = "";
            if (value > 0.0) {
                price = value + "";
                Log.d("priceTAG",price);

            } else {
                price = "INVALID API REQUEST CALL";
            }

            Log.d("OutPutTestingTagg",price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return price;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});

       final TextView currentStockPrice = (TextView) findViewById(R.id.StockPrice);
        Button StockPrice = (Button) findViewById(R.id.removeStock);
        StockPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText enterNumStock = (EditText) findViewById(R.id.editText2);
                final TextView totalStockWorth = (TextView) findViewById(R.id.StockDrop);
                String s1,CurrentStocPrice;
                float n1,n2;
                s1=enterNumStock.getText().toString();
                n1=Float.parseFloat(s1);
                CurrentStocPrice=price+"";
                n2=Float.parseFloat(CurrentStocPrice);

                currentStockPrice.setText(price);
                amount=s1+"";

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                Calculations c = new Calculations();
                total = c.calckNetValue(n2,n1)+"";
                totalStockWorth.setText("You have "+total+" value of price");

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                HashMap<String, String> transactionHistory = new HashMap<String, String>();
                HashMap<String, String> transaction = new HashMap<String, String>();

                transaction.put("amount", amount);
                transaction.put("name", stockName);
                transaction.put("price", price);
                transaction.put("time", timeStamp);
                transaction.put("total", total);
                transaction.put("type", "Stock");

                mDatabase.child("users").child(uid).child("transactionHistory").push().setValue(transaction);

            }
        });


        //remove stock investment
        Button removeStock= (Button) findViewById(R.id.RemoveStock);
        removeStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText enterNumStock = (EditText) findViewById(R.id.editText2);
                final TextView totalStockWorth = (TextView) findViewById(R.id.StockDrop);
                String s1,CurrentStocPrice;
                float n1,n2;
                s1=enterNumStock.getText().toString();
                n1=Float.parseFloat(s1);
                CurrentStocPrice=price+"";
                n2=Float.parseFloat(CurrentStocPrice);

                currentStockPrice.setText(price);
                amount=s1+"";

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                Calculations c = new Calculations();
                total = c.calckNetValue(n2,n1)+"";
                totalStockWorth.setText("You have "+total+" value of price");

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                HashMap<String, String> transactionHistory = new HashMap<String, String>();
                HashMap<String, String> transaction = new HashMap<String, String>();

                String removeStockAmount = (Integer.parseInt(amount)*-1)+"";
                transaction.put("amount", removeStockAmount);
                transaction.put("name", stockName);
                transaction.put("price", price);
                transaction.put("time", timeStamp);
                transaction.put("total", total);
                transaction.put("type", "Stock");

                mDatabase.child("users").child(uid).child("transactionHistory").push().setValue(transaction);

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("transactionHistory");

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0, j=0, k=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    dateAsString = snapshot.child("time").getValue().toString();
                    Date theSameDate = new Date();
                    try{
                        theSameDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateAsString);

                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    Log.d("timeTAG", dateAsString);
                    totalAsString = snapshot.child("total").getValue().toString();
                    float theSameFloat = Float.valueOf(totalAsString);

                    if(snapshot.child("name").getValue().toString().equals(stockName1)){
                        dates1.add(theSameDate);
                        totals1.add(theSameFloat);
                    }
                    else if(snapshot.child("name").getValue().toString().equals(stockName2)){
                        dates2.add(theSameDate);
                        totals2.add(theSameFloat);
                    }
                    else if(snapshot.child("name").getValue().toString().equals(stockName3)){
                        dates2.add(theSameDate);
                        totals3.add(theSameFloat);
                    }
                    else
                        Log.d("invalidStockNameError", "ERROR: Invalid Stock Name");
                }

                //Graph params
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.setTitle("Test Title");
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph.getGridLabelRenderer().setVerticalAxisTitle("Price");

                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(dates1.get(0), totals1.get(0)),
                        new DataPoint(dates1.get(1), totals1.get(1)),
                        new DataPoint(dates1.get(2), totals1.get(2)),
                        new DataPoint(dates1.get(3), totals1.get(3)),
                        new DataPoint(dates1.get(4), totals1.get(4)),


                });
                graph.addSeries(series1);
                series1.setDrawDataPoints(true);
                series1.setTitle("Test Title 1");
                series1.setColor(Color.RED);

                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {

                });
                graph.addSeries(series2);
                series2.setDrawDataPoints(true);
                series2.setTitle("Test Title 2");
                series2.setColor(Color.RED);

                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {


                });
                graph.addSeries(series3);
                series3.setDrawDataPoints(true);
                series3.setTitle("Test Title 3");
                series3.setColor(Color.GREEN);

                // set date label formatter
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 5 because of the space

                // set manual x bounds to have nice steps
                graph.getViewport().setMinX(dates1.get(0).getTime());
                graph.getViewport().setMaxX(dates1.get(4).getTime());
                graph.getViewport().setMinY(0.0);
                graph.getViewport().setMaxY(10000.0);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setScalable(false);
                graph.getViewport().setScrollable(true);

                // as we use dates as labels, the human rounding to nice readable numbers
                // is not necessary
                graph.getGridLabelRenderer().setHumanRounding(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.setting:
                Toast.makeText(this,"We are Student of GSU",Toast.LENGTH_LONG).show();
                break;
            case R.id.setting1:
                ToHomePage();
                break;
            case R.id.setting2:
                ToStock();
                break;
            case R.id.setting3:
                ToCrypto();
                break;

            case R.id.setting4:
                ToBank();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ToStock() {
        Toast.makeText(this,"You are in Stock Page",Toast.LENGTH_LONG).show();

    }

    private void ToHomePage() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

    }

    private void ToCrypto() {
        startActivity(new Intent(getApplicationContext(),Cryptocurrency.class));

    }

    private void ToBank() {
        startActivity(new Intent(getApplicationContext(),Bank.class));
    }

}
