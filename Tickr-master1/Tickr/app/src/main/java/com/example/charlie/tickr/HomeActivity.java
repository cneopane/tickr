package com.example.charlie.tickr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    Button logOut;
    private Button Bank1,Crypto1,Stock1;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    float bankTotal, stockTotal, coinTotal;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home Page");

        Bank1 =(Button)findViewById(R.id.Bank);
        Crypto1 =(Button)findViewById(R.id.Crypto);
        Stock1 =(Button)findViewById(R.id.Stock);
        Crypto1.setOnClickListener(this);
        Bank1.setOnClickListener(this);
        Stock1.setOnClickListener(this);





        logOut = (Button) findViewById(R.id.btn_LogOut);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("UserID",user.getUid());
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
            String uid = user.getUid();
            Log.d("UNICHECK",uid);
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

        DatabaseReference mDatabase2;

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("portfolios");
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("testingLog","working1?");


                Log.d("testingLog","dsdsdsd");
                Double bankTotalS = (Double) dataSnapshot.child("bank").child("total").getValue();
                String bankTotalSS = bankTotalS+"";
               bankTotal = Float.valueOf(bankTotalSS);
//bankTotal=23.5f;
                Double stockTotalS = (Double) dataSnapshot.child("stocks").child("total").getValue();
                String stockTotalSS = stockTotalS+"";
               stockTotal = Float.valueOf(stockTotalSS);
               //stockTotal = 23.4f;
                Double coinTotalS = (Double) dataSnapshot.child("coins").child("total").getValue();
                String coinTotalSS = coinTotalS+"";
               coinTotal = Float.valueOf(coinTotalSS);
               //coinTotal = 45.3f;
                Log.d("pieTAG", bankTotal+" "+stockTotal+" "+coinTotal);

                PieChartView pieChart = (PieChartView) findViewById(R.id.chart);
                PieChartData pieData;

                List<SliceValue> values = new ArrayList<SliceValue>();
                SliceValue bankSliceValue = new SliceValue((float) bankTotal, Color.rgb(237,37,78));
                SliceValue stocksSliceValue = new SliceValue((float)stockTotal, Color.rgb(131,182,146));
                SliceValue coinsSliceValue = new SliceValue((float)coinTotal, Color.rgb(108,190,237));
                values.add(bankSliceValue);
                values.add(stocksSliceValue);
                values.add(coinsSliceValue);


                pieData = new PieChartData(values);
                pieData.setHasLabels(true);
                pieData.setHasLabelsOnlyForSelected(false);
                pieData.setHasLabelsOutside(false);
                pieData.setHasCenterCircle(false);


                pieChart.setPieChartData(pieData);
                pieChart.setChartRotationEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("pieTAG2", bankTotal+" "+stockTotal+" "+coinTotal);


        float allTotals = bankTotal + stockTotal + coinTotal;

        float pleasework = (float)bankTotal;
        Log.d("vslueTag",stockTotal+"");

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


        }
        return super.onOptionsItemSelected(item);
    }

    private void ToHomePage() {
        Toast.makeText(this,"You are already in HomePage",Toast.LENGTH_LONG).show();

    }


}

