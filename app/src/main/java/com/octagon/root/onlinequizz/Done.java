package com.octagon.root.onlinequizz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octagon.root.onlinequizz.Common.Common;
import com.octagon.root.onlinequizz.Model.Question;
import com.octagon.root.onlinequizz.Model.QuestionScore;

import customfonts.MyTextView;

public class Done extends AppCompatActivity {

    TextView txtResultScore,getTxtResultQuestion;
    ProgressBar progressBar;
    MyTextView btnTryAgain;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);

        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (MyTextView) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();

            }
        });

        //get data from bundle and to view
        Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //upload score to DB
            question_score.child(String.format("%s_%s", Common.currentUser.getUsername(),
                                                            Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername(),
                            Common.categoryId),
                            Common.currentUser.getUsername(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));

        }
    }
}
