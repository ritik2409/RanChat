package com.example.android.ranchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatTogether extends AppCompatActivity {

    LinearLayout layout;
    Button sendButton;
    EditText messageArea;
    ScrollView scrollView;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myref1;
    DatabaseReference myref2;
    private ArrayList<String> messages;
    SessionManager session;
    private String username;
    String chatwith;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_together);

        Intent i = getIntent();
        chatwith = i.getExtras().getString("chatwith");
        auth = FirebaseAuth.getInstance();

        layout = (LinearLayout)findViewById(R.id.layout1);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        messages = new ArrayList<>();
        session = new SessionManager(getApplicationContext());
        messageArea = (EditText) findViewById(R.id.message_area);

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);
        database = FirebaseDatabase.getInstance();


        sendButton = (Button) findViewById(R.id.send);

        myref1 = database.getReference(username+"_"+chatwith);
        myref2 = database.getReference(chatwith+"_"+username);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", username);
                    myref1.push().setValue(map);
                    myref2.push().setValue(map);
                }
            }
        });

        myref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(username)){
                    addMessageBox("You:-\n" + message, 1);
                }
                else{
                    addMessageBox(chatwith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatTogether.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);

        if(type == 1) {
            lp.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.rounded_corner1);

        }
        else {
            lp.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.rounded_corner2);

        }
        textView.setLayoutParams(lp);


        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}

