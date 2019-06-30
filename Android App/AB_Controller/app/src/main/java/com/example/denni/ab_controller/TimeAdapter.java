package com.example.denni.ab_controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder>  {
    Context context;
    private List<Time> timeList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView id, stat, desc, from, until, satz;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            id = (TextView) view.findViewById(R.id.row_id);
            stat = (TextView) view.findViewById(R.id.status);
            desc = (TextView) view.findViewById(R.id.row_desc);
            from = (TextView) view.findViewById(R.id.row_from);
            until = (TextView) view.findViewById(R.id.row_until);
            satz = (TextView) view.findViewById(R.id.stundensatz);
        }
        @Override
        public void onClick(View v) {

            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            try {


                jsonObject.put("$class", "composer.base.acceptTime");
                jsonObject.put("status", true);
                jsonObject.put("time", "resource:composer.base.Time#" + timeList.get(getAdapterPosition()).getId());



            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(jsonObject);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences.getString("Role","").equals("Kunde")){
                JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                        (Request.Method.POST, preferences.getString("Url","")+"/api/composer.base.acceptTime", jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();


                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

// Add the request to the RequestQueue.
                queue.add(jsonObjRequest);

            }




        }

    }



    public TimeAdapter(List<Time> timeList) {
        this.timeList = timeList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Time car = timeList.get(position);
            holder.desc.setText(car.Id);
            holder.id.setText(car.desc);
            holder.stat.setText(car.status.toString());
            holder.from.setText(car.from);
            holder.until.setText(car.until);
            String from = car.from;
            String until = car.until;
            String[] separated = from.split(":");
            from = separated[0];
            String[] separated2 = until.split(":");
            until = separated2[0];
            int hours = Integer.parseInt(until) - Integer.parseInt(from);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String gesamt = ""+hours * Integer.parseInt(preferences.getString("satz","100"));
            holder.satz.setText(gesamt + " Euro");
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        System.out.println(timeList.size());
        return timeList.size();
    }


}