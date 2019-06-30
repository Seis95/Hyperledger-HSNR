package com.example.denni.ab_controller;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder>  {
    Context context;
    private List<Project> projectList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView id, desc, status, fNr, from, until;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            id = (TextView) view.findViewById(R.id.row_id);
            desc = (TextView) view.findViewById(R.id.row_desc);
            status = (TextView) view.findViewById(R.id.status);
            from = (TextView) view.findViewById(R.id.row_from);
            until = (TextView) view.findViewById(R.id.row_until);
        }
        @Override
        public void onClick(View v) {
            time(projectList.get(getAdapterPosition()).getId(),projectList.get(getAdapterPosition()).getSatz());
        }

    }

    public void time(String id, String satz){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("projectId",id);
        editor.putString("satz",satz);
        editor.apply();
        Intent myIntent = new Intent(context, TimeActivity.class);
        context.startActivity(myIntent);
    }

    public ProjectAdapter(List<Project> projectList) {
        this.projectList = projectList;
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
        Project car = projectList.get(position);
        holder.id.setText(car.getId());
        holder.status.setText(car.getName().toString());
        holder.desc.setText(car.getDesc().toString());

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


}