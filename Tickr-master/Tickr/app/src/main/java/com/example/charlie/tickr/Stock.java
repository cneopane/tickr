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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

public class Stock extends AppCompatActivity {
    
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        getSupportActionBar().setTitle("Stock Value");

        final TextView SP = (TextView) findViewById(R.id.StockPrice);
        Button StockPrice = (Button) findViewById(R.id.FindStock);
        StockPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Stocks s = new Stocks("MSFT");

                new Thread(s).start();
                try {
                    //System.out.printf(s.returnAPIresponse("hello"));
                    double value = s.getStockPrice("MSFT");
                    String price = "";
                    if (value > 0.0) {
                        price = value + "";
                        SP.setText(price);

                    } else {
                        price = "INVALID API REQUEST CALL";
                    }

                    Log.d("OutPutTestingTagg",price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final EditText ed = (EditText) findViewById(R.id.editText2);
                final TextView sp = (TextView) findViewById(R.id.StockDrop);
                String s1,s2;
                float n1,n2;
                s1=ed.getText().toString();
                s2=SP.getText().toString();
                n1=Float.parseFloat(s1);
                n2=Float.parseFloat(s2);
                Calculations c = new Calculations();

                sp.setText("You have "+c.calckNetValue(n2,n1)+" value of price");


//    public void testThis(){
//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Stock s = new Stock("MSFT");
//                try  {
//                    //System.out.printf(s.returnAPIresponse("hello"));
//                    double re = s.getStockPrice("MSFT");
//                    String price = "";
//                    if(re > 0.0){
//                        price = re+"";
//                    }else{
//                        price = "INVALID API REQUEST CALL";
//                    }
//
//                    Log.d("OutPutTestingTag", price);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }
            }

        });

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d8 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d9 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d10 = calendar.getTime();


        //Graph params
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Test Title");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Price");

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 4),
                new DataPoint(d5, 5),
                new DataPoint(d6, 1),
                new DataPoint(d7, 5),
                new DataPoint(d8, 3),
                new DataPoint(d9, 4),
                new DataPoint(d10, 5)
        });
        graph.addSeries(series1);
        series1.setDrawDataPoints(true);
        series1.setTitle("Test Title");
        series1.setColor(Color.BLACK);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 4),
                new DataPoint(d2, 3),
                new DataPoint(d3, 2),
                new DataPoint(d4, 1),
                new DataPoint(d5, 0),
                new DataPoint(d6, 1),
                new DataPoint(d7, 2),
                new DataPoint(d8, 3),
                new DataPoint(d9, 4),
                new DataPoint(d10, 5)
        });

        graph.addSeries(series2);
        series2.setDrawDataPoints(true);
        series2.setTitle("Test Title 2");
        series2.setColor(Color.RED);


// set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(d5.getTime());
        graph.getViewport().setMaxX(d10.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

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
                ToBank();


        }
        return super.onOptionsItemSelected(item);
    }

    private void ToBank() {
        startActivity(new Intent(getApplicationContext(),Bank.class));
    }


}



