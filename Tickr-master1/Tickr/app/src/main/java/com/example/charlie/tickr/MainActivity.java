package com.example.charlie.tickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


/**
 * Created by charlie on 3/25/18.
 */

public class MainActivity extends AppCompatActivity {
    SignInButton button;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2001;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;



    protected void onStart(){
        super.onStart();
//if login one time, it will automatically login
        mAuth.addAuthStateListener(mAuthListener);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (SignInButton) findViewById(R.id.google_login);
        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn();
            }

        });

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(firebaseAuth.getCurrentUser() !=null){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();



    }


    private void signIn() {

        Intent signInIntent =Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                // Google Sign In was successful, authenticate with Firebase
                firebaseAuthWithGoogle(account);

            } else  {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Failed to Authentication", Toast.LENGTH_SHORT).show();

                // ...
            }
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew;
                            DatabaseReference mDatabase;


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));

                            if(isNew) {
                                //MAKE THE HASHMAP HERE

                                mDatabase = FirebaseDatabase.getInstance().getReference();

                                HashMap<String, String> info = new HashMap<String, String>();
                                info.put("email", user.getEmail());
                                info.put("name", user.getDisplayName());
                                mDatabase.child(uid).child("info").setValue(info);

                                HashMap<String, String> bank = new HashMap<String, String>();
                                bank.put("checking", String.valueOf(0.00));
                                bank.put("savings", String.valueOf(0.00));
                                bank.put("total", String.valueOf(0.00));
                                mDatabase.child(uid).child("portfolios").child("bank").setValue(bank);

                                HashMap<String, String> coins = new HashMap<String, String>();
                                coins.put("total", String.valueOf(0.00));
                                mDatabase.child(uid).child("portfolios").child("coins").setValue(coins);

                                HashMap<String, String> stocks = new HashMap<String, String>();
                                stocks.put("total", String.valueOf(0.00));
                                mDatabase.child(uid).child("portfolios").child("stocks").setValue(stocks);
                            }
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            user = mAuth.getCurrentUser();


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}


