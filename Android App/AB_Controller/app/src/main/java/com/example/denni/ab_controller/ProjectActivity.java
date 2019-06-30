package com.example.denni.ab_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectActivity extends AppCompatActivity {
    private List<Project> List = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProjectAdapter mAdapter;
    private Button new_project;
    private Button action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_car);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        new_project = (Button) findViewById(R.id.new_project);
        action = (Button) findViewById(R.id.button5);
        mAdapter = new ProjectAdapter(List);
        mAdapter.setContext(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!preferences.getString("Role","").equals("Admin")){
            new_project.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
        }

        getList();
        prepareData();
    }

    private void prepareData() {

        mAdapter.notifyDataSetChanged();
    }

    public void action (View v){
        Intent myIntent = new Intent(getApplicationContext(), CreateUserActivity.class);
        startActivity(myIntent);
    }
    public void project (View v){
        Intent myIntent = new Intent(getApplicationContext(), CreateProjectActivity.class);
        startActivity(myIntent);
    }

    public void getList (){

        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        JsonArrayRequest jsonObjRequest = new JsonArrayRequest
                (Request.Method.GET, preferences.getString("Url","")+"/api/composer.base.Project",null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Project project = null;
                        try {
                            ObjectMapper m = new ObjectMapper();
                            for (int y = 0; y <= response.length(); y++){
                                project = m.readValue(response.getJSONObject(y).toString(), Project.class);
                                List.add(project);
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
