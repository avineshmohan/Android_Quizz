package com.octagon.root.onlinequizz;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octagon.root.onlinequizz.Common.Common;
import com.octagon.root.onlinequizz.Model.User;
import com.octagon.root.onlinequizz.Model.Ussr;

import java.util.jar.Attributes;

import customfonts.MyEditText;
import customfonts.MyTextView;

public class Signin extends AppCompatActivity {

    MyEditText User, Password;
    Button signin;
    ImageView sb;
    FirebaseDatabase database;
    DatabaseReference users , exploit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        exploit = database.getReference("Exploit");

        User = (MyEditText) findViewById(R.id.User);
        Password = (MyEditText) findViewById(R.id.Password);
        signin = (Button)findViewById(R.id.signin);
        sb = (ImageView)findViewById(R.id.sinb);
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        signIn(User.getText().toString(),Password.getText().toString());

                        String username = User.getText().toString();
                        String pass = Password.getText().toString();

                        sup();
                        User.setText("");
                        Password.setText("");
            }
        });
    }

    private void sup() {

        final Ussr user = new Ussr(User.getText().toString(),
                Password.getText().toString());

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
//                    Toast.makeText(Signin.this, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    users.child(user.getUsername())
                            .setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists())
                {
                    if(!user.isEmpty())
                    {
                        com.octagon.root.onlinequizz.Model.User login = dataSnapshot.child(user).getValue(User.class);
                        if(login.getPassword().equals(pwd)) {
                            Intent homeActivity = new Intent(Signin.this, Home.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();
                        }
                        else
                            Toast.makeText(Signin.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Signin.this, "Please Enter your User name", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Signin.this, "User is not Exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
