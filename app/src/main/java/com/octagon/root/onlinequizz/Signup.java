package com.octagon.root.onlinequizz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octagon.root.onlinequizz.Model.User;

import customfonts.MyEditText;
import customfonts.MyTextView;

public class Signup extends AppCompatActivity {
    ImageView sback;
    MyEditText NewUser, NewPassword, NewEmail, Name;
    MyTextView signup;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sback = (ImageView) findViewById(R.id.sback);

        NewUser = (MyEditText)findViewById(R.id.NewUser);
        NewPassword = (MyEditText)findViewById(R.id.NewPassword);
        NewEmail = (MyEditText)findViewById(R.id.NewEmail);
        signup = (MyTextView)findViewById(R.id.signup);

        Name= (MyEditText) findViewById(R.id.fname);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(Signup.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


    }

    private void signUp() {
        final User user = new User(NewUser.getText().toString(),
                NewPassword.getText().toString(),
                NewEmail.getText().toString(),
                Name.getText().toString());

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    Toast.makeText(Signup.this, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    users.child(user.getUsername())
                            .setValue(user);
                    signin();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void signin() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Signup.this);
        alertDialogBuilder
                .setMessage("User Registration Successful!!!")
                .setCancelable(false)
                .setPositiveButton("Sign In",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent signin = new Intent(Signup.this ,Signin.class);
                                startActivity(signin);
                                finish();

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
