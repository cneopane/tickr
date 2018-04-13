package com.example.charlie.tickr;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Cryptocurrency extends AppCompatActivity implements View.OnClickListener{
    String stockName="";
    String price, amount, timeStamp, total;
    String dateAsString, totalAsString;
    String stockName1 = "MSFT", stockName2 = "APPL", stockName3 = "GOOG";

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
        setContentView(R.layout.activity_cryptocurrency);
        getSupportActionBar().setTitle("CryotoCurrency");

        Spinner spinner = (Spinner) findViewById(R.id.DropDown);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Cryptocurrency.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.DropDown1));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        stockName = "BTC";
                        returnPriceOfSelectedStock(stockName);
                        break;
                    case 1:
                        stockName = "ETH";
                        returnPriceOfSelectedStock(stockName);
                        break;
                    case 2:
                        stockName = "XRP";
                        returnPriceOfSelectedStock(stockName);
                        break;

                }


            }

            public String returnPriceOfSelectedStock(String stockName){

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Cryptos s = new Cryptos(stockName);

                new Thread(s).start();
                try {
                    //System.out.printf(s.returnAPIresponse("hello"));
                    double value = s.getCoinPrice(stockName);
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

        final TextView currentCoinPrice = (TextView) findViewById(R.id.StockPrice);
        Button StockPrice = (Button) findViewById(R.id.FindStock);
        StockPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText enterNumStock = (EditText) findViewById(R.id.editText2);
                final TextView totalStockWorth = (TextView) findViewById(R.id.StockDrop);
                String s1;
                float n1,n2;
                s1=enterNumStock.getText().toString();
                n1=Float.parseFloat(s1);
                n2=Float.parseFloat(price);
                currentCoinPrice.setText(price);
                amount=s1+"";

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
                transaction.put("type", "Coin");

                mDatabase.child("transactionHistory").push().setValue(transaction);

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


    @Override
    public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.Bank:
                    ToHomePage();
                    break;
                case R.id.Stock:
                    ToStock();
                    break;
                case R.id.Home:
                    ToHomePage();
                    break;
            }
    }
}
