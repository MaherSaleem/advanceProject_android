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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AfterLoggedIn extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.1.7:8080/Project/";
    TextView weclomeTextView;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_logged_in);
        weclomeTextView = (TextView) findViewById(R.id.welcome_text_view);
        weclomeTextView.setText("Welcome " + GlobalClass.studentName);
        ll = (LinearLayout) findViewById(R.id.questions_linearlayout);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);

        Call<List<Question>> repos = apiService.getQuestions("1");//Quiz #1

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
                Toast.makeText(AfterLoggedIn.this, "error", Toast.LENGTH_LONG).show();
            }
        });


    }


    public void onclick_submit(View v_) {
        int questionsSize = ll.getChildCount();
        int score = 0;

        for(int i = 0 ; i < questionsSize ; i++){
            View v = ll.getChildAt(i);
            TextView correct_answer_text_view = (TextView)v.findViewById(R.id.correct_ans_text_view);
            int correctAns = Integer.parseInt(correct_answer_text_view.getText().toString());

            RadioButton ans1_radio_btn = (RadioButton) v.findViewById(R.id.ans1);
            RadioButton ans2_radio_btn = (RadioButton) v.findViewById(R.id.ans2);
            RadioButton ans3_radio_btn = (RadioButton) v.findViewById(R.id.ans3);

            if (correctAns == 1 &&  ans1_radio_btn.isChecked()
                    || correctAns == 2 &&  ans2_radio_btn.isChecked()
                    || correctAns == 3 &&  ans3_radio_btn.isChecked())
                        score++;
        }
            Log.w("***1" ,"you have get " + score + " from " + questionsSize );

    }

    /*
    this function is not used
     */
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}
