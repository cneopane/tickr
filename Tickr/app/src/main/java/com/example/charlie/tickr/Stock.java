package com.example.charlie.tickr;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Stock extends AppCompatActivity {


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
                Stocks s = new Stocks("MSFT");
                try {
                    //System.out.printf(s.returnAPIresponse("hello"));
                    double re = s.getStockPrice("MSFT");
                    String price = "";
                    if (re > 0.0) {
                        price = re + "";
                        SP.setText(price);
                    } else {
                        price = "INVALID API REQUEST CALL";
                    }

                    Log.d("OutPutTestingTag",price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        }
        return super.onOptionsItemSelected(item);
    }
}


