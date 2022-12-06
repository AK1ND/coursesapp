package com.example.coursesapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursesapp.Course;
import com.example.coursesapp.R;

import java.util.ArrayList;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    Context context;

    ArrayList<Course> list;

    public CatalogAdapter(Context context, ArrayList<Course> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_catalog, parent, false);

        return new CatalogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {

        Course course = list.get(position);
        holder.name.setText(course.getName());
        holder.description.setText(course.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;


        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_courseName);
            description = itemView.findViewById(R.id.tv_description);
        }
    }


}
