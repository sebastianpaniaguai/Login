package com.example.sebas.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    CallbackManager callbackManager;
    SignInButton GLoginButton;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    DatabaseHelper databaseHelper=new DatabaseHelper(this);
    private static final int RC_SIGN_IN=9001;
    private static final String TAG="MainActivity";
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private String name;
    private static final int PREFERENCE_MODE_PRIVATE=0;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor=preferenceSettings.edit();

        if(preferenceSettings.getBoolean("flag",flag)){
            Intent i = new Intent(MainActivity.this, Mostrar.class);
            i.putExtra("nombre", preferenceSettings.getString("name",name));
            startActivity(i);
        }
        final TextView welcome=(TextView) findViewById(R.id.tWelcome);
        final TextView info=(TextView) findViewById(R.id.info);
        final TextView or=(TextView) findViewById(R.id.tor);
        final EditText User=(EditText) findViewById(R.id.eUser);
        final EditText Pass=(EditText) findViewById(R.id.ePass);
        final Button sign=(Button) findViewById(R.id.bSign);
        final Button log=(Button) findViewById(R.id.bLog);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.fblogin_button);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        GLoginButton = (SignInButton) findViewById(R.id.GLoginButton);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/TimKid.ttf");
        welcome.setTypeface(myCustomFont);
        info.setTypeface(myCustomFont);
        User.setTypeface(myCustomFont);
        Pass.setTypeface(myCustomFont);
        sign.setTypeface(myCustomFont);
        or.setTypeface(myCustomFont);
        loginButton.setTypeface(myCustomFont);
        log.setTypeface(myCustomFont);
        sign.setTypeface(myCustomFont);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname=User.getText().toString();
                String password=Pass.getText().toString();

                String Password=databaseHelper.searchPass(uname);
                if(password.equals(Password)){
                    preferenceEditor.putBoolean("flag",true);
                    preferenceEditor.putString("name",uname);
                    preferenceEditor.commit();
                    Intent i = new Intent(MainActivity.this, Mostrar.class);
                    i.putExtra("nombre",uname);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password doesn't match with username",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

        callbackManager= CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if( accessToken != null ){
            Intent i = new Intent(MainActivity.this, Mostrar.class);
            startActivity(i);
        }



        GLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    RequestData();
                }
                Intent i = new Intent(MainActivity.this, Mostrar.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // disconnect api if it is connected
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    public void RequestData(){
        GraphRequest request= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json=response.getJSONObject();
                try {
                    if (json!=null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters= new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if(result.isSuccess()){
            acct=result.getSignInAccount();
            //ABRIR LA NUEVA ACTIVIDAD DESPUES DE QUE SE HA LOGUEADO CON GOOGLE
            name=getString(R.string.signed_int_fmt, acct.getDisplayName());
            preferenceEditor.putBoolean("flag",true);
            preferenceEditor.putString("name",name);
            preferenceEditor.commit();
            Intent i = new Intent(MainActivity.this, Mostrar.class);
            i.putExtra("nombre", getString(R.string.signed_int_fmt,acct.getDisplayName()));
            startActivity(i);
        }
    }


    public void signIn(){
        Intent singInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(singInIntent,RC_SIGN_IN);
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
