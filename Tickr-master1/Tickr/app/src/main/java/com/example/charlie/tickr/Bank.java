package com.example.charlie.tickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

//graphview libraries


public class Bank extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    private ListView mUserList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    private Button Home1,Crypto1,Stock1;
    private ArrayList<String> mUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        getSupportActionBar().setTitle("Bank");
        final TextView sp = (TextView) findViewById(R.id.Display_name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        sp.setText(name);

        Home1 =(Button)findViewById(R.id.Home);
        Crypto1 =(Button)findViewById(R.id.Crypto);
        Stock1 =(Button)findViewById(R.id.Stock1);
        Crypto1.setOnClickListener(this);
        Home1.setOnClickListener(this);
        Stock1.setOnClickListener(this);

        mUserList = (ListView) findViewById(R.id.user_list);

        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mUsernames);

        mUserList.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("transactionHistory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String test = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    test = "";
//                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {
//                        test += snapshot.getValue().toString();
//                    }
                    String amount = snapshot.child("amount").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String time = snapshot.child("time").getValue().toString();
                    String total = snapshot.child("total").getValue().toString();
                    String type = snapshot.child("type").getValue().toString();

                    Log.d("doesitwork", type);

                    int len = ("Asset Type: "+type).length();
                    int len2 = ("Total: "+total).length();
                    int len3 = ("Amount: " + amount).length();
                    String tab = "";
                    for(int i =0;i<Math.abs(len-len2);i++) {
                        tab+="\t";
                    }
                    String tab2 = "";
                    for(int i =0;i<Math.abs(len-len3);i++) {
                        tab2+="\t";
                    }
                    mUsernames.add("Asset Type: "+ type+ "\n Name: " + name +
                            "\nTotal: "+total+"\n"+tab+" Price: "+ price +
                            "\nAmount: " + amount +"\n"+tab2+ " Time: "+ time);

//                    mUsernames.add(test);
                    arrayAdapter.notifyDataSetChanged();
                }
//                mUsernames.add(test);
//                arrayAdapter.notifyDataSetChanged();
                Log.d("onDataChange: ", test);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Home:
                ToHomePage();
                break;
            case R.id.Stock1:
                ToStock();
                break;
            case R.id.Crypto:
                ToCrypto();
                break;
        }

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

    private void ToBank() {
        Toast.makeText(this,"You are already in Bank Page",Toast.LENGTH_LONG).show();

    }

    private void ToHomePage() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

    }
    private void ToStock() {
        startActivity(new Intent(getApplicationContext(),Stock.class));

    }
    private void ToCrypto() {
        startActivity(new Intent(getApplicationContext(),Cryptocurrency.class));

    }
}

