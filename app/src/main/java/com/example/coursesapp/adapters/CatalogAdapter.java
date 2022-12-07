package com.example.coursesapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursesapp.Course;
import com.example.coursesapp.CourseActivity;
import com.example.coursesapp.HomeActivity;
import com.example.coursesapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    Context context;

    ArrayList<Course> list;

    String fromFragment;

    public CatalogAdapter(Context context, ArrayList<Course> list, String fromFragment) {
        this.context = context;
        this.list = list;
        this.fromFragment = fromFragment;
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

        String idCourse = course.getId();


        holder.storageReference.child("coursesimages/" + idCourse).getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imageView)
                )
                .addOnFailureListener(e -> {
                });

        holder.cardView.setOnClickListener(view -> {

            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("key", idCourse);
            intent.putExtra("fromFragment", fromFragment);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView imageView;
        StorageReference storageReference;
        CardView cardView;
        DatabaseReference databaseReference;


        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_courseName);
            description = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.imgView_course);
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            cardView = itemView.findViewById(R.id.cardViewElement);

        }
    }


}
