package com.example.zacarias.smartlibraries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class searchResults extends Activity {
    Intent intent;
    ListView listView;
    ArrayList libraryArr = new ArrayList<String>();
    ArrayList selectedCasesLib = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        intent = getIntent();
        try {
            final JSONObject jsonIncom = new JSONObject(intent.getStringExtra("json"));
            libraryArr = arrayObtainer(jsonIncom);
            listView = (ListView) findViewById(R.id.list_view1);
            search(intent.getStringExtra("string"));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selected = (String) selectedCasesLib.get(position);
                    int realPos = libraryArr.indexOf(selected);

                    try {
                        JSONArray libraries = jsonIncom.getJSONArray("Students");
                        JSONObject jsonselected = libraries.getJSONObject(realPos);
                        Intent inte3 = new Intent(getApplicationContext(), Ficha.class);
                        inte3.putExtra("json", jsonselected.toString());
                        startActivity(inte3);
                    } catch (JSONException e) {
                    }

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    //Obtener array de bibliotecas
    public ArrayList<String> arrayObtainer(JSONObject json) {
        ArrayList<String> auxArray = new ArrayList<>();
        try {
            JSONArray libraries = json.getJSONArray("Students");

            //for extractor
            for (int i = 0; i < libraries.length(); i++) {
                auxArray.add(libraries.getJSONObject(i).getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxArray;
    }


    public void search(String frase) {
        Iterator it = libraryArr.iterator();
        if (!selectedCasesLib.isEmpty()) {
            selectedCasesLib.clear();
        }
        while (it.hasNext()) {
            String aux = (String) it.next();
            if (aux.toLowerCase().contains(frase.toLowerCase())) {
                selectedCasesLib.add(aux);
            }
        }

        if (selectedCasesLib.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No coincidences found.",
                    Toast.LENGTH_LONG).show();
                    finish();
        } else {
            //Marca A
            SearchedAdapted arrayAdapter = new SearchedAdapted(getApplicationContext(), selectedCasesLib);

            listView.setAdapter(arrayAdapter);

        }


    }
}
