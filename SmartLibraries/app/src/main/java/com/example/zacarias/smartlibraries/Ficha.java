package com.example.zacarias.smartlibraries;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Ficha extends Activity {

    Intent intent;
    String URL;
    JSONObject jsonData;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha);


        intent = getIntent();
        TextView text = (TextView) findViewById(R.id.ficha_name);


        try {
            json = new JSONObject(intent.getStringExtra("json"));
            readWebpage();
            String name = (String) json.getString("Name");
            text.setText(name);
            URL = getURL(name);
            text = (TextView) findViewById(R.id.ficha_timetable);
            String timetable = (String) json.getString("Timetable");
            String source = "<b>" + "Timetable: " + "</b>" + timetable;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_address);
            String address = json.getString("Adress");
            source = "<b>" + "Address: " + "</b>" + address;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_access);
            String access = (String) json.getString("Accessibility");
            source = "<b>" + "Accessibility: " + "</b>" + access;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_phone);
            String phone = (String) json.getString("Phone");
            source = "<b>" + "Phone: " + "</b>" + phone;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_web);
            String web = (String) json.getString("Web");
            source = "<b>" + "Web: " + "</b>" + web;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_wifi);
            String wifi = (String) json.getString("Wifi");
            source = "<b>" + "Wifi: " + "</b>" + wifi;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_capacity);
            String capacity = (String) json.getString("Aforo");
            source = "<b>" + "Capacity: " + "</b>" + capacity;
            text.setText(Html.fromHtml(source));
            text = (TextView) findViewById(R.id.ficha_room);
            String rooms = (String) json.getString("Study room booking available");
            source = "<b>" + "Study room booking available: " + "</b>" + rooms;
            text.setText(Html.fromHtml(source));


            WebView webView = (WebView) findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("http://maps.google.com/maps/api/staticmap?center=" + json.getString("Lat") + "," + json.getString("Longi") + "&zoom=16&size=400x400&sensor=false");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ficha, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getURL(String name) {
        //insertar la URL correctaa URL de la base de datos de Cloudant
        return "URL";
    }

    public void readWebpage() {
        //Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();
        DownloadAsyn task = new DownloadAsyn();
        task.execute(new String[]{"https://6eeb4e08-932a-4dc0-9db8-6c6f4026a074-bluemix.cloudant.com/pi/_design/timeStamp/_view/new-view?include_docs=true&limit=1&descending=true"});

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

                    //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json1 = new JSONObject(result);
                JSONArray json2 = json1.getJSONArray("rows");
                JSONObject json3 = json2.getJSONObject(0);
                JSONObject json4 = json3.getJSONObject("doc");
                jsonData = json4.getJSONObject("d");
                //Toast.makeText(getApplicationContext(), jsonData.toString(), Toast.LENGTH_LONG).show();


                TextView text = (TextView) findViewById(R.id.ficha_sound);
                String sound = jsonData.getString("noise");
                String source = "<b>" + "Noise level: " + "</b>" + sound;
                text.setText(Html.fromHtml(source));
                ProgressBar prog = (ProgressBar) findViewById(R.id.progress_sound);
                Double value = Double.valueOf(jsonData.getString("noise"));
                if(value < 33){
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));
                }
                else if(value < 66){
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.yellowprogressbar));
                }
                else{
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.redprogressbar));
                }
                prog.setProgress(value.intValue());


                text = (TextView) findViewById(R.id.ficha_people);
                String people = jsonData.getString("people");
                source = "<b>" + "People in the library: " + "</b>" + people;
                text.setText(Html.fromHtml(source));
                prog = (ProgressBar) findViewById(R.id.progress_people);
                value = Double.valueOf(jsonData.getString("people"))/Double.valueOf(json.getString("Aforo"))*100;
                if(value < 33){
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogressbar));

                }
                else if(value < 66){
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.yellowprogressbar));
                }
                else{
                    prog.setProgressDrawable(getResources().getDrawable(R.drawable.redprogressbar));
                }

                prog.setProgress(value.intValue());
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
