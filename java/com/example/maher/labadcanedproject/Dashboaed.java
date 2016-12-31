package com.example.maher.labadcanedproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboaed extends AppCompatActivity {

    Intent startQuizIntent;
    TextView weclomeTextView, nameOfActiveQuiz, upcommingTextview;
    Button enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboaed);

        getSupportActionBar().setTitle("Main Page");  // provide compatibility to all the versions

        enroll = (Button) findViewById(R.id.btn_enroll);
        weclomeTextView = (TextView) findViewById(R.id.welcome_text_view);
        nameOfActiveQuiz = (TextView) findViewById(R.id.name_of_active_quiz);
        upcommingTextview = (TextView) findViewById(R.id.upcomming_textview);

        weclomeTextView.setText("Welcome " + GlobalClass.studentName);// print your name

        startQuizIntent = new Intent(this, QuizQuestions.class);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);


        /*
        *   get the open quiz(if there any)
        */
        Call<List<Quiz>> repos = apiService.getActiveQuizes();

        repos.enqueue(new Callback<List<Quiz>>() {

            @Override
            public void onResponse(Call<List<Quiz>> Call, Response<List<Quiz>> response) {
                int statusCode = response.code();
                List<Quiz> quizes = response.body();
                if (quizes.size() < 1) {//no quizes
                    enroll.setEnabled(false);
                    Log.w("***$", "No quizes");
                    nameOfActiveQuiz.setText("No Opene Quizes");

                } else {
                    int Quiz_id = quizes.get(0).getQid(); //assuming just one quiz
                    startQuizIntent.putExtra("quiz_id", Quiz_id);
                    nameOfActiveQuiz.setText("Quiz number" + Quiz_id + " is opened");
                    Log.w("***$", quizes.get(0).getQid() + "");
                }

            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                Toast.makeText(Dashboaed.this, "error", Toast.LENGTH_LONG).show();

            }


        });

        //get the list of upcomming quizes
        repos = apiService.getUpcommingQuizes();

        repos.enqueue(new Callback<List<Quiz>>() {

            @Override
            public void onResponse(Call<List<Quiz>> Call, Response<List<Quiz>> response) {
                int statusCode = response.code();
                List<Quiz> quizes = response.body();
                if (quizes.size() < 1) {//no quizes
                    Log.w("***$", "No upcomming quizes");
                    upcommingTextview.setText("No Upcomming quizes");

                } else {
                    String text = "";
                    for (Quiz q : quizes) {
                        text = text + "Quiz" + q.getQid() + " : " + q.getStartTime() + "\n";
                    }
                    upcommingTextview.setText(text);

                }

            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                Toast.makeText(Dashboaed.this, "error", Toast.LENGTH_LONG).show();

            }


        });
    }


    /*
    *this is invoked when we back from quiz
    * its used to refresh the activity
     */
    @Override
    public void onRestart() {
        super.onRestart();
        Intent previewMessage = new Intent(Dashboaed.this, Dashboaed.class);
        startActivity(previewMessage);
        this.finish();
    }

    /*
    * used to lauch the activity for a quiz
     */
    public void onclick_enroll_quiz(View v) {
        startActivity(startQuizIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                //this code is used to refresh the activity
                Intent previewMessage = new Intent(Dashboaed.this, Dashboaed.class);
                startActivity(previewMessage);
                Toast.makeText(Dashboaed.this, "Page refreshed", Toast.LENGTH_SHORT).show();
                this.finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
