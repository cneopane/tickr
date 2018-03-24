package com.example.charlie.tickr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout Profile;
    private Button Signout,Bank1,Crypto1,Stock1;
    private SignInButton SignIn;
    private TextView Name,Email;
    private GoogleApiClient googleAliClient;
    private static final int REQ_CODE = 0623;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Profile = (LinearLayout) findViewById(R.id.profile);
        Signout =(Button)findViewById(R.id.btn_LogOut);
        Bank1 =(Button)findViewById(R.id.Bank);
        Crypto1 =(Button)findViewById(R.id.Crypto);
        Stock1 =(Button)findViewById(R.id.Stock);
        SignIn=(SignInButton)findViewById(R.id.google_login);
        Name = (TextView)findViewById(R.id.Display_name);
        Email =(TextView)findViewById(R.id.Display_email);
        SignIn.setOnClickListener(this);
        Signout.setOnClickListener(this);
        Bank1.setOnClickListener(this);
        Crypto1.setOnClickListener(this);
        Stock1.setOnClickListener(this);
        Profile.setVisibility(View.GONE);



        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleAliClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


    }


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
    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.google_login:
            signIn();
            break;
        case R.id.Bank:
            ToBank();
            break;
        case R.id.Stock:
            ToStock();
            break;
        case R.id.Crypto:
            ToCrypto();
            break;
        case R.id.btn_LogOut:
            singOut();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn()
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleAliClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void singOut()
    {
       Auth.GoogleSignInApi.signOut(googleAliClient).setResultCallback(new ResultCallback<Status>() {
           @Override
           public void onResult(@NonNull Status status) {
               updateUI(false);
           }
       });

    }

    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {

            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            Name.setText(name);
            Email.setText(email);
            updateUI(true);
        }else
        {
            updateUI(false);
        }

    }
    private void updateUI(boolean isLogin)
    {
        if(isLogin)
        {
            Profile.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }
        else
        {
            Profile.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
