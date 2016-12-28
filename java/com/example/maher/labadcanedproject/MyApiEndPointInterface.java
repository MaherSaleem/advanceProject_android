package com.example.maher.labadcanedproject;


import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Maher on 12/26/2016.
 */
public interface MyApiEndPointInterface {

    @GET("students/{user_id}")
    Call<Student> getStudentInfo(@Path("user_id") String userId);

    @GET("quizes/{quiz_id}")
    Call<List<Question>> getQuestions(@Path("quiz_id") String quizId);

    @GET("quizes/active")
    Call<List<Quiz>> getActiveQuizes();

    @GET("quizes/upcomming")
    Call<List<Quiz>> getUpcommingQuizes();

    @FormUrlEncoded
    @POST("grades/set_grade")
    Call<String> setStudentGrade(@Field("stu_id")  int stuId,
                                 @Field("quiz_id")        int quizId,
                                 @Field("grade")  double grade
                                 );

        @GET("quizes/remainingTime/{quiz_id}")
        Call<String> getRemmingTimeForQuiz(@Path("quiz_id") String quiz_id);

}


