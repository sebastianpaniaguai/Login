package com.example.sebas.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class Mostrar extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_mostrar);
        tv= (TextView) findViewById(R.id.usrName);
        String name=getIntent().getStringExtra("nombre");
        tv.setText(name);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if( accessToken != null ){
            Profile p=Profile.getCurrentProfile();
            String n;
            if(p!=null){
                n=p.getName();
            }
            else{
                n=" ";
            }
            tv.setText(n);
        }
    }
}
