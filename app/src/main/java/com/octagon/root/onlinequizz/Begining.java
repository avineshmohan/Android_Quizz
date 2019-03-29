package com.octagon.root.onlinequizz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octagon.root.onlinequizz.Common.Common;
import com.squareup.picasso.Picasso;

import customfonts.MyTextView;

public class Begining extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 9900;
    final static long TIMEOUT = 60000;
    int progressValue = 0;

    CountDownTimer mCountDown;

    int index=0,score=0,thisQuestion=0,totalQuestion,correctAnswer;



    ProgressBar progressBar;
    ImageView question_image;
    MyTextView btnA,btnB,btnC,btnD;
    TextView question_text,txtScore,txtQuestionNUm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begining);



        //Views
        txtScore = (TextView)findViewById(R.id.txtscore);
        txtQuestionNUm = (TextView)findViewById(R.id.txtTotalQuestion);

        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        btnA = (MyTextView) findViewById(R.id.btnAnswerA);
        btnB = (MyTextView) findViewById(R.id.btnAnswerB);
        btnC = (MyTextView) findViewById(R.id.btnAnswerC);
        btnD = (MyTextView) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();
        if(index <totalQuestion) //still there having Questions
        {
            MyTextView clickedButton = (MyTextView) view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                //choose correct answer
                score+=1;
                correctAnswer++;
                showQuestions(++index);
            }
            else
            {
               wrongAnswer();
            }

            txtScore.setText(String.format("%d",score));
        }


    }

    private void wrongAnswer() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Begining.this);
        alertDialogBuilder
                .setMessage("Sorry !! Wrong Answer")
                .setCancelable(false)
                .setPositiveButton("Next",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                showQuestions(++index);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showQuestions(int index) {
        if(index < totalQuestion)
        {
            thisQuestion++;
            txtQuestionNUm.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true"))
            {
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else
            {
                question_text.setText(Common.questionList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start(); //Start timer
        }
        else
        {
            // if final

            Intent intent = new Intent(this,Done.class);
            Bundle datasend = new Bundle();
            datasend.putInt("SCORE",score);
            datasend.putInt("TOTAL",totalQuestion);
            datasend.putInt("CORRECT",correctAnswer);
            intent.putExtras(datasend);
            startActivity(intent);
            finish();
        }

    }

    //press

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();

        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long l) {
                progressBar.setProgress(progressValue);
                progressValue++;


            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestions(++index);

            }
        };
        showQuestions(index);
    }
}
