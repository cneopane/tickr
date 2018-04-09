package com.example.charlie.tickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    Button logOut;
    private Button Bank1,Crypto1,Stock1;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bank1 =(Button)findViewById(R.id.Bank);
        Crypto1 =(Button)findViewById(R.id.Crypto);
        Stock1 =(Button)findViewById(R.id.Stock);
        Crypto1.setOnClickListener(this);
        Bank1.setOnClickListener(this);
        Stock1.setOnClickListener(this);





        logOut = (Button) findViewById(R.id.btn_LogOut);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final TextView nM = (TextView) findViewById(R.id.Display_name);
            final TextView eM = (TextView) findViewById(R.id.Display_email);
            // Name, email address
            String name = user.getDisplayName();
            String email = user.getEmail();

            nM.setText(name);
            eM.setText(email);

            // Check if user's email is verified
            //boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken()
            //String uid = user.getUid();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            }
        };
        logOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        //PIE CHART

        PieChartView pieChart = (PieChartView) findViewById(R.id.chart);
        PieChartData pieData;

        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < 6; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
            values.add(sliceValue);
        }

        pieData = new PieChartData(values);
        pieData.setHasLabels(true);
        pieData.setHasLabelsOnlyForSelected(false);
        pieData.setHasLabelsOutside(false);
        pieData.setHasCenterCircle(false);


        pieChart.setPieChartData(pieData);
        pieChart.setChartRotationEnabled(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Bank:
                ToBank();
                break;
            case R.id.Stock:
                ToStock();
                break;
            case R.id.Crypto:
                ToCrypto();
                break;
        }

    }

    private void ToBank() {
        startActivity(new Intent(getApplicationContext(),Bank.class));
    }
    private void ToStock() {
        startActivity(new Intent(getApplicationContext(),Stock.class));
    }
    private void ToCrypto() {
        startActivity(new Intent(getApplicationContext(),Cryptocurrency.class));
    }
}

