package com.example.zacarias.smartlibraries;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readWebpage();
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash_screen);


        // Simulate a long loading process on application startup.

    }

    public void readWebpage() {
        DownloadAsyn task = new DownloadAsyn();
        task.execute(new String[]{"https://e5324516-1b4f-417a-b9ca-e777efc3467b-bluemix:1e036c2595e0a3ee468916b2caa1ce923d3ae483dc7674fb8ce84c31543675ed@e5324516-1b4f-417a-b9ca-e777efc3467b-bluemix.cloudant.com/library/63ea61170ffc480749698dee19d0fe1a"});

    }

    public class DownloadAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response = response + "\n" + s;

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("json", json.toString());
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}