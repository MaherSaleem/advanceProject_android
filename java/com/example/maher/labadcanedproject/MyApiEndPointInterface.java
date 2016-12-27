package com.example.maher.labadcanedproject;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maher on 12/26/2016.
 */
public interface MyApiEndPointInterface {

    @GET("students/{user_id}")
    Call<Student> getStudentInfo(@Path("user_id") String userId);

    @GET("quizes/{quiz_id}")
    Call<List<Question>> getQuestions(@Path("quiz_id") String quizId);

}
