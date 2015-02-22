package com.example.zacarias.smartlibraries;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NearestList extends Activity {
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_list);


        try {
            json = new JSONObject(getIntent().getExtras().getString("json"));
            JSONArray jsona = json.getJSONArray("Students");
            double min;
            int pos;
            final JSONArray arrayJson = new JSONArray();
            for (int i = 0; i < jsona.length(); i++) {
                min = getDistance(jsona.getJSONObject(i));
                pos = 0;
                for (int j = 0; j < jsona.length(); j++) {
                    if (min > getDistance(jsona.getJSONObject(j))) {
                        pos++;
                    }
                }
                arrayJson.put(pos, jsona.getJSONObject(i));
            }


            final String[] biblios = new String[arrayJson.length()];
            for (int i = 0; i < arrayJson.length(); i++) {
                biblios[i] = arrayJson.getJSONObject(i).getString("Name");
            }

            final ListView list = (ListView) findViewById(R.id.listView);
            final NearestListAdapter adapter = new NearestListAdapter(getApplicationContext(), biblios);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == adapter.getCount() - 1) {
                        if (adapter.getCount() == biblios.length + 1) {
                            //Toast.makeText(getBaseContext(), "No more", Toast.LENGTH_LONG).show();
                        } else {

                            adapter.more();
                            list.setAdapter(adapter);
                            //list.set
                            list.setSelectionFromTop(position, 20);
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Ficha.class);
                        try {
                            intent.putExtra("json", arrayJson.getJSONObject(position).toString());
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

            try {
                LocationGetter getter = new LocationGetter(this.getApplicationContext());
                Location myLocations = getter.getLocation();


            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearest_list, menu);
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

    public double getDistance(JSONObject json) throws JSONException {
        double longMob = 0, latMob = 0;
        double lat = Double.valueOf(json.getString("Lat"));
        double longi = Double.valueOf(json.getString("Longi"));

        try {
            LocationGetter getter = new LocationGetter(this.getApplicationContext());
            Location myLocations = getter.getLocation();
            longMob = myLocations.getLongitude();
            latMob = myLocations.getLatitude();

        } catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        double rest1 = Math.abs(longMob - longi);
        double rest2 = Math.abs(latMob - lat);

        double distance = Math.sqrt(rest1 * rest1 + rest2 * rest2);
        return distance;
    }
}
