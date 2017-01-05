package com.example.maher.labadcanedproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends Activity {


    TextView userId;
    TextView passWord;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check sharedpreferces
        SharedPreferences myShared = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        if (myShared.getInt("studentId", -1) != -1) {//the student have logged in before
            GlobalClass.studentId = myShared.getInt("studentId", -1);
            GlobalClass.studentName = myShared.getString("studentName", "NONE");
            Intent intent = new Intent(MainActivity.this, Dashboaed.class);
            MainActivity.this.startActivity(intent);

        }

        userId = (TextView) findViewById(R.id.user_id);
        passWord = (TextView) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentId = userId.getText().toString().trim();
                final String password = passWord.getText().toString().trim();


                Retrofit retrofit = new Retrofit.Builder().baseUrl(GlobalClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                MyApiEndPointInterface apiService = retrofit.create(MyApiEndPointInterface.class);

                Call<Student> repos = apiService.getStudentInfo(studentId);//the studnet ID

                repos.enqueue(new Callback<Student>() {

                    @Override
                    public void onResponse(Call<Student> Call, Response<Student> response) {
                        int statusCode = response.code();
                        Student studentObject = response.body();
                        if (studentObject.getId() == Integer.parseInt(studentId) && password.compareTo(studentObject.getPassword()) == 0) {
                            Toast.makeText(MainActivity.this, "Correct User", Toast.LENGTH_SHORT).show();
                            GlobalClass.studentId = Integer.parseInt(studentId);
                            GlobalClass.studentName = studentObject.getName();

                            // store username info into sharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("studentId", GlobalClass.studentId);
                            editor.putString("studentName", GlobalClass.studentName);
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, Dashboaed.class);
                            MainActivity.this.startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong user Name or password", Toast.LENGTH_SHORT).show();
                        }
                        Log.w("***", studentObject.toString());
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        // Log error here since request failed
                        Toast.makeText(MainActivity.this, "Can't reach the server, try to turn of firewall", Toast.LENGTH_LONG).show();
                    }


                });
            }
        });


    }
}
