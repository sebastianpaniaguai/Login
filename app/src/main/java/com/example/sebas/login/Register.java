package com.example.sebas.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText username;
    EditText pass;
    EditText confirm;
    Button signin;
    DatabaseHelper databaseHelper=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText) findViewById(R.id.eName);
        email=(EditText) findViewById(R.id.email);
        username=(EditText) findViewById(R.id.eUsername);
        pass=(EditText) findViewById(R.id.ePass);
        confirm=(EditText) findViewById(R.id.eConfirm);
        signin=(Button) findViewById(R.id.SignButton);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/TimKid.ttf");
        name.setTypeface(myCustomFont);
        email.setTypeface(myCustomFont);
        username.setTypeface(myCustomFont);
        pass.setTypeface(myCustomFont);
        confirm.setTypeface(myCustomFont);
        signin.setTypeface(myCustomFont);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                if(!pass.getText().toString().equals(confirm.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Passwords doesn't match", Toast.LENGTH_LONG).show();
                }
                else{
                    //Insert data in database
                    Contact c=new Contact();
                    c.setName(name.getText().toString());
                    c.setEmail(email.getText().toString());
                    c.setUsername(username.getText().toString());
                    c.setPassword(pass.getText().toString());

                    databaseHelper.insertContact(c);
                }
                Toast.makeText(getApplicationContext(), "User succesfully stored!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
}
