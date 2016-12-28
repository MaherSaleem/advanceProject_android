package com.example.maher.labadcanedproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.os.Handler;

public class QuizQuestions extends AppCompatActivity {

    LinearLayout ll; // to inseart questions into it
    int QuizId = 0;
    TextView remaingTimeTextView; // for times

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        ll = (LinearLayout) findViewById(R.id.questions_linearlayout);
        remaingTimeTextView = (TextView) findViewById(R.id.remaining_time_textview);

        //get data from the pervious activity(this data is quiz id)
        Intent intent = getIntent();
        QuizId = intent.getIntExtra("quiz_id", -1);
        Log.w("***quizId***", QuizId + "");
        getSupportActionBar().setTitle("Quiz#" + QuizId);  // provide compatibility to all the versions

//=============================================================================================
        /*
        make a request to get remaining time for the quiz
         */
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);

        Call<String> responseForRemaingTime = apiService.getRemmingTimeForQuiz(QuizId + "");
        responseForRemaingTime.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int statusCode = response.code();
                String time = response.body();
                remaingTimeTextView.setText(time);
                startTimer();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(QuizQuestions.this, "error in getting the time", Toast.LENGTH_LONG).show();
                Log.w("***", t.toString());
            }
        });
//=============================================================================================
        //get all qustions for that quiz
        //http://stackoverflow.com/questions/32617770/how-to-get-response-as-string-using-retrofit-without-using-gson-or-any-other-lib
        retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(MyApiEndPointInterface.class);
        Call<List<Question>> repos = apiService.getQuestions(String.valueOf(QuizId));//Quiz #1

        repos.enqueue(new Callback<List<Question>>() {

            @Override
            public void onResponse(Call<List<Question>> Call, Response<List<Question>> response) {
                int statusCode = response.code();
                List<Question> questions = response.body();

                //this object used to inflate layout
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);


                //travese all questions
                for (int i = 0; i < questions.size(); i++) {
                    Question q = questions.get(i); // get the ith question

                    View v = inflater.inflate(R.layout.question_card_view, null);

                    TextView question_text_view = (TextView) v.findViewById(R.id.question_textview);
                    question_text_view.setText(q.getQuestionNumber() + ")  " + q.getQuestion());

                    RadioButton ans1_text_view = (RadioButton) v.findViewById(R.id.ans1);
                    ans1_text_view.setText(q.getAns1());

                    RadioButton ans2_text_view = (RadioButton) v.findViewById(R.id.ans2);
                    ans2_text_view.setText(q.getAns2());

                    RadioButton ans3_text_view = (RadioButton) v.findViewById(R.id.ans3);
                    ans3_text_view.setText(q.getAns3());

                    TextView correct_ans_text_view = (TextView) v.findViewById(R.id.correct_ans_text_view);
                    correct_ans_text_view.setText(q.getCorrectAns() + "");

                    ll.addView(v);
                }


            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(QuizQuestions.this, "error", Toast.LENGTH_LONG).show();
            }
        });
//=============================================================================================
    }


    /*
    *this is used to periodiclly check the time.
    * time is checked every 5 seconds.
    */
    Handler handler;
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            String newRemaining = remaingTimeTextView.getText().toString();
            int seconds = convertTimeToSeconds(newRemaining);
            seconds -= 5;//subtract 5 seconds from cyrrent time
            newRemaining = convertSecondsIntoTimeFomat(seconds);
            remaingTimeTextView.setText(newRemaining);

            /*
                Make a request to
                check if the quiz finished(or deactivated by teacher)
             */
            Retrofit retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);

            Call<String> responseForRemaingTime = apiService.getRemmingTimeForQuiz(QuizId + "");
            responseForRemaingTime.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int statusCode = response.code();
                    String time = response.body();
                    if (time.compareTo("DONE") == 0) {
                        Log.w("***done", "done");
                        checkGrades();//correcting the answers
                        handler.removeCallbacks(runnableCode);//stop the thread
                        finish();//close the activity

                    } else
                        Log.w("***done", "still active");

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(QuizQuestions.this, "error in getting the time", Toast.LENGTH_LONG).show();
                    Log.w("***", t.toString());
                }
            });

            //repeat the process every 5 seconds
            handler.postDelayed(runnableCode, 5000);
        }
    };

    public void startTimer() {
        /*
        decreement counter and check if time finshed
         */
        //https://guides.codepath.com/android/Repeating-Periodic-Tasks
        // Create the Handler object (on the main thread by default)
        handler = new Handler();
        handler.post(runnableCode);

    }

    /*
    * this function is used to clculate the grade for the stuednt
     */
    public void checkGrades() {
        int questionsSize = ll.getChildCount();
        int score = 0;

        for (int i = 0; i < questionsSize; i++) {
            View v = ll.getChildAt(i);
            TextView correct_answer_text_view = (TextView) v.findViewById(R.id.correct_ans_text_view);
            int correctAns = Integer.parseInt(correct_answer_text_view.getText().toString());

            RadioButton ans1_radio_btn = (RadioButton) v.findViewById(R.id.ans1);
            RadioButton ans2_radio_btn = (RadioButton) v.findViewById(R.id.ans2);
            RadioButton ans3_radio_btn = (RadioButton) v.findViewById(R.id.ans3);

            if (correctAns == 1 && ans1_radio_btn.isChecked()
                    || correctAns == 2 && ans2_radio_btn.isChecked()
                    || correctAns == 3 && ans3_radio_btn.isChecked())
                score++;
        }
        Log.w("***1", "you have get " + score + " from " + questionsSize);
        Toast.makeText(QuizQuestions.this, "you have get " + score + " from " + questionsSize, Toast.LENGTH_LONG).show();

        //edit the grade on the server
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);

        Call<String> repos = apiService.setStudentGrade(GlobalClass.studentId, QuizId, ((double) (score)) / questionsSize);

        repos.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        finish();

    }

    public void onclick_submit(View v_) {
        checkGrades();
    }


    /*
    * convert string of format "HH:MM:SS" to seconds
     */
    public int convertTimeToSeconds(String time) {
        int seconds;
        String[] data = time.split(":");
        Log.w("***hours***", data[0]);
        seconds = Integer.parseInt(data[0]) * 3600 + Integer.parseInt(data[1]) * 60 + Integer.parseInt(data[2]);
        return seconds;
    }

    /*
    * convert string of seonds to "HH:MM:SS" format
     */
    public String convertSecondsIntoTimeFomat(int seconds) {

        if (seconds <= 0) return "00:00:00";

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d:%02d", hours, minutes, sec);

    }
}
