package com.example.charlie.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Stocks s = new Stocks("MSFT");
                try  {
                    //System.out.printf(s.returnAPIresponse("hello"));
                    double re = s.getStockPrice("MSFT");
                    String price = "";
                    if(re > 0.0){
                        price = re+"";
                    }else{
                        price = "INVALID API REQUEST CALL";
                    }

                    Log.d("OutPutTestingTag", price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
