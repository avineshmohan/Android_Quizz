package com.octagon.root.onlinequizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octagon.root.onlinequizz.Common.Common;
import com.octagon.root.onlinequizz.Model.Question;

import java.util.Collection;
import java.util.Collections;

import customfonts.MyTextView;

public class Start extends AppCompatActivity {

    MyTextView btnBegin;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        
        loadQuestions(Common.categoryId);

        btnBegin = (MyTextView) findViewById(R.id.btnBegin);

        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,Begining.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void loadQuestions(String categoryId) {
        //to clear old question
        if(Common.questionList.size() > 0)
            Common.questionList.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //Randon list
        Collections.shuffle(Common.questionList);
    }
}
