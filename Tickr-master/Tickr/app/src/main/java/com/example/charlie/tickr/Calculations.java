package com.example.charlie.tickr;

/**
 * Created by Austin Tyler on 3/24/2018.
 */

public class Calculations{


    private float netValue, userNetWorth;

    //Takes amount of stock owned and value per stock as inputs and returns total value
    public float calckNetValue(float amount, float value){
        netValue = amount * value;
        return netValue;
    }

    //Takes net value of users stocks, cryptocurrencies, and savings as inputs and returns their net value.
    public float calcUserNetWorth(float stockNet, float cryptoNet, float bankNet){
        userNetWorth = stockNet + cryptoNet + bankNet;
        return userNetWorth;
    }
}
