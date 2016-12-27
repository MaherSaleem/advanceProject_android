package com.example.maher.labadcanedproject;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Maher on 12/27/2016.
 */
public class HttpManager {
    public static String getData(String URI) {
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(URI);
            HttpURLConnection httpURLConnection = (HttpURLConnection)
                    url.openConnection();
            bufferedReader = new BufferedReader(new
                    InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line + '\n');
                line = bufferedReader.readLine();
            }
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());


            return stringBuilder.toString();
        } catch (Exception ex) {
            Log.d("HttpURLConnection", ex.toString());
        }
        return null;
    }
}
