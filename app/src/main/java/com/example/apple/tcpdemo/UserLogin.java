package com.example.apple.tcpdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by apple on 4/20/18.
 */

public class UserLogin extends AppCompatActivity {

    private  EditText mUsernameView;
    private  EditText mPasswordView;
    private String mUsername;
    private String mPassword;
    private String mName;
    private Socket mSocket;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);


        login = (TextView) findViewById(R.id.btn_login);
        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        //mSocket.connect();
        // Set up the login form.
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                mSocket.emit("test", "awesome");

            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        mSocket.connect();

        mUsernameView = (EditText) findViewById(R.id.et_email);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_register || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.btn_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        TextView register = (TextView) findViewById(R.id.tv_register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            });


        mSocket.on("login", onLogin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("login", onLogin);
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mUsernameView.setError(getString(R.string.app_name));
            mUsernameView.requestFocus();
            return;
        }

        mUsername = username;
        mPassword = password;

        JSONObject jb = new JSONObject();
        try{

            jb.put("username",mUsername);
            jb.put("password",mPassword);
            Log.e("jb",""+jb);
        }catch (Exception e){
            e.printStackTrace();
        }

        // perform the user login attempt.
        mSocket.emit("login", jb);

    }

    String response;
    JSONObject data;


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args)
        {
            data = (JSONObject) args[0];
            Log.e("json",""+data.toString());
            if(data.length()>0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast();
                    }
                });
            }

        }
    };

    public void toast(){

        Log.e("dataobj",""+data);

        try {

            response =data.getString("result");
            if (response.equals("success")) {
                //String message = data.getString("message");
                mName = data.getString("name");
                Toast.makeText(getApplicationContext(),"you have successfully logged in",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("username", mUsername);
                intent.putExtra("password", mPassword);
                intent.putExtra("name",mName);
                startActivity(intent);
            }else{
               // String message = data.getString("message");
                Toast.makeText(getApplicationContext(),"username or password worng!!!",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            return;
        }


    }
}
