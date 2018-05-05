package com.example.android.ranchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth authb;
    private FirebaseUser user;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private String email2;
    private String username;
    SessionManager session;
    String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        session = new SessionManager(getApplicationContext());
        authb = FirebaseAuth.getInstance();

    }


    public void signinUsr(View view) {
        email2 = email.getText().toString();
        password2 = password.getText().toString();
        if (TextUtils.isEmpty(email2)) {
            //email is empty}
            Toast.makeText(LoginActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            //password is empty
            Toast.makeText(LoginActivity.this, "Please Enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging User...");
        progressDialog.show();


        authb.signInWithEmailAndPassword(email2, password2).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = authb.getCurrentUser();
                    if(password2.equals("example1"))
                        username = "person1";
                    if(password2.equals("example2"))
                        username = "person2";
                    if(password2.equals("example3")) username = "person3";
                    session.createLoginSession(username,email2);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                }
            }
        });
    }
}