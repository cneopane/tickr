package com.example.charlie.tickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Cryptocurrency extends AppCompatActivity {

    private Button mFirebaseBtn;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptocurrency);


        mFirebaseBtn = (Button) findViewById(R.id.FindStock);

        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("Name").setValue("Haroon");

            }
        });




    }

}
