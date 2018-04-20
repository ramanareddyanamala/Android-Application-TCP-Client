package com.example.apple.tcpdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;



public class ProfileActivity extends AppCompatActivity  {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private Button mBtChangePassword;
    private Button mBtLogout;
    private String mToken;
    private String mEmail;
    private String mUsername;
    private String mName;
    private String mPassword;
    Button button;
    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        button = (Button) findViewById(R.id.btn_logout);
        change = (Button) findViewById(R.id.btn_change_password);
        Intent intent = getIntent();

        mUsername = intent.getStringExtra("username");
        mPassword = intent.getStringExtra("password");
        mName = intent.getStringExtra("name");

        mTvName.setText("Name : "+mName);
        mTvEmail.setText("Email : "+mUsername);
        mTvDate.setText("password : "+mPassword);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Changepassword.class);
                intent.putExtra("username", mUsername);
                intent.putExtra("password", mPassword);
                intent.putExtra("name",mName);
                startActivity(intent);
                //startActivity(new Intent(getApplicationContext(),UserLogin.class));
            }
        });
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),UserLogin.class));
           }
       });

    }
}

