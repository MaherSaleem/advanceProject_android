package com.example.maher.labadcanedproject;

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

public class MainActivity extends AppCompatActivity {


    TextView userId;
    TextView passWord;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login page");  // provide compatibility to all the versions

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
                        if (studentObject.getId() == Integer.parseInt(studentId) && password.compareTo(studentObject.getPassword()) == 0 ) {
                            Toast.makeText(MainActivity.this, "Correct User", Toast.LENGTH_SHORT).show();
                            GlobalClass.studentId = Integer.parseInt(studentId);
                            GlobalClass.studentName = studentObject.getName();
                            Intent intent = new Intent(MainActivity.this , Dashboaed.class);
                            MainActivity.this.startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong user Name or password", Toast.LENGTH_SHORT).show();
                        }
                        Log.w("***", studentObject.toString());
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        // Log error here since request failed
                        Toast.makeText(MainActivity.this, "No internet Conncetion", Toast.LENGTH_LONG).show();
                    }


                });
            }
        });


    }
}
//                c = db.getStudent(studentName);
//                try {
//                    if (!(!(c.moveToFirst()) || c.getCount() == 0)) {
//
//                        if (c.getString(1).equals(studentName) && c.getString(2).equals(password)) {
//                            Intent intent = new Intent(MainActivity.this, UserPage.class);
//                            SharedPreferences.Editor editor = getSharedPreferences("MY_PREFRENCCES", MODE_PRIVATE).edit();
//                            int id = c.getInt(0);
//                            editor.putInt("ID", id);
//                            editor.apply();
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(MainActivity.this, "NOT FOUND", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "IN CATCH", Toast.LENGTH_SHORT).show();
//
//                }
////                String name = re
//            }
