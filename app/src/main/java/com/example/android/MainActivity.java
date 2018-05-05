package com.example.android.ranchat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth authb;
    TextView person1;
    TextView person2;
    TextView person3;
    private String email;
    private String username;
    SessionManager session;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        username = user.get(SessionManager.KEY_USERNAME);

        authb = FirebaseAuth.getInstance();

        person1 = (TextView) findViewById(R.id.person1);
        person2 = (TextView) findViewById(R.id.person2);
        person3 = (TextView) findViewById(R.id.person3);
        ViewGroup viewGroup = (ViewGroup) person1.getParent();
        if(username.equals("person1"))
        {
            viewGroup.removeView(person1);
        }
        if(username.equals("person2"))
        {
            viewGroup.removeView(person2);
        }
        if(username.equals("person3"))
        {
            viewGroup.removeView(person3);
        }


    }

    public void chat(View view)
    { String chatwith;
        if(view == person1)
            chatwith="person1";
        else if(view == person2)
            chatwith="person2";
        else chatwith="person3";

        Intent i = new Intent(MainActivity.this, ChatTogether.class);
        i.putExtra("chatwith",chatwith);
        startActivity(i);
        finish();



    }

    public void account(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("My Account");
        alertDialogBuilder.setMessage("username:" + username + "\n" + email).setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void logout(View view) {
        progressDialog.setMessage("Logging out...");
        progressDialog.show();

        authb.signOut();
        session.logoutUser();
    }

}
