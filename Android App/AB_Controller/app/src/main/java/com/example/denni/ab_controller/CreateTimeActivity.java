package com.example.denni.ab_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTimeActivity extends AppCompatActivity {
    EditText fahrnr, kennzeichen, km, pos, from, until;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        km = (EditText) findViewById(R.id.create_kennzeichen);
        kennzeichen = (EditText) findViewById(R.id.create_kennzeichen);
        fahrnr = (EditText) findViewById(R.id.create_time_id);
        kennzeichen.setVisibility(View.GONE);
        km.setVisibility(View.GONE);
        pos = (EditText) findViewById(R.id.create_time_desc);
        from = (EditText) findViewById(R.id.from);
        until = (EditText) findViewById(R.id.until);
    }

    public void safe(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            jsonObject.put("$class", "composer.base.Time");
            jsonObject.put("Id", fahrnr.getText().toString());
            jsonObject.put("projectId", preferences.getString("projectId",""));
            jsonObject.put("personId", preferences.getString("Id",""));
            jsonObject.put("desc", pos.getText().toString());
            jsonObject.put("from", from.getText().toString());
            jsonObject.put("status", false);
            jsonObject.put("until", until.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.POST, preferences.getString("Url","")+"/api/composer.base.Time", jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplicationContext(), TimeActivity.class);
                        startActivity(myIntent);

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


        queue.add(jsonObjRequest);

    }
}
