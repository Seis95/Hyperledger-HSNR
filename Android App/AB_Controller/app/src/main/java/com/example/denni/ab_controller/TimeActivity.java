package com.example.denni.ab_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeActivity extends AppCompatActivity {
    private List<Time> List = new ArrayList<>();
    private RecyclerView recyclerView;
    private TimeAdapter mAdapter;
    private Button new_project;
    private Button action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_car);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        new_project = (Button) findViewById(R.id.new_project);
        action = (Button) findViewById(R.id.button5);
        mAdapter = new TimeAdapter(List);
        mAdapter.setContext(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        new_project.setVisibility(View.INVISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!preferences.getString("Role","").equals("Mitarbeiter")){
            new_project.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
        }
        if (preferences.getString("Role","").equals("Admin")){
            action.setVisibility(View.VISIBLE);
        }
        getList();
        prepareData();
    }

    private void prepareData() {

        mAdapter.notifyDataSetChanged();
    }

    public void action (View v){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getString("Role","").equals("Mitarbeiter")){
            Intent myIntent = new Intent(getApplicationContext(), CreateTimeActivity.class);
            startActivity(myIntent);
        }
        if (preferences.getString("Role","").equals("Admin")){
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                    (Request.Method.DELETE, preferences.getString("Url","")+"/api/composer.base.Project/"+preferences.getString("projectId",""), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

// Add the request to the RequestQueue.
            queue.add(jsonObjRequest);

        }

    }

    public void getList (){

        RequestQueue queue = Volley.newRequestQueue(this);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JsonArrayRequest jsonObjRequest = new JsonArrayRequest
                (Request.Method.GET, preferences.getString("Url","")+"/api/composer.base.Time",null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                            Time time = null;
                            try {
                                if (response.getJSONObject(0) != null) {
                                ObjectMapper m = new ObjectMapper();
                                for (int y = 0; y < response.length(); y++) {
                                    time = m.readValue(response.getJSONObject(y).toString(), Time.class);
                                    if (preferences.getString("Role","").equals("Mitarbeiter")) {
                                        if (time.personId.equals(preferences.getString("Id",""))){
                                            if (time.projectId.equals(preferences.getString("projectId",""))){
                                                List.add(time);
                                            }
                                        }
                                    } else {
                                        if (time.projectId.equals(preferences.getString("projectId",""))){
                                            List.add(time);
                                        }
                                    }
                                }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            mAdapter.notifyDataSetChanged();
                        }

                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                               Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

// Add the request to the RequestQueue.
        queue.add(jsonObjRequest);



    }
}
