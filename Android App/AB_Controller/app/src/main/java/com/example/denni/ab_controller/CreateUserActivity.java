package com.example.denni.ab_controller;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class CreateUserActivity extends AppCompatActivity {
    EditText tradeid, name, nachname, passwort, rolle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        tradeid = (EditText) findViewById(R.id.project_id);
        name = (EditText) findViewById(R.id.project_name);
        nachname = (EditText) findViewById(R.id.project_desc);
        passwort = (EditText) findViewById(R.id.create_password);
        rolle = (EditText) findViewById(R.id.create_role);
    }

    public void safe(View v)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {


            jsonObject.put("$class", "composer.base.Person");
            jsonObject.put("Id", tradeid.getText().toString());
            jsonObject.put("firstName", name.getText().toString());
            jsonObject.put("lastName",nachname.getText().toString());
            jsonObject.put("password", passwort.getText().toString());
            jsonObject.put("Role", rolle.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.POST, preferences.getString("Url","")+"/api/composer.base.Person", jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplicationContext(), ProjectActivity.class);
                        startActivity(myIntent);

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
