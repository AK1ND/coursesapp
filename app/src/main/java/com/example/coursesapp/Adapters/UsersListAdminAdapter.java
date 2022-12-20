package com.example.coursesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coursesapp.CourseActivity;
import com.example.coursesapp.R;
import com.example.coursesapp.data.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UsersListAdminAdapter extends RecyclerView.Adapter<UsersListAdminAdapter.UserListAdminViewHolder> {

    Context context;

    ArrayList<User> list;

    String fromFragment;

    public UsersListAdminAdapter(Context context, ArrayList<User> list, String fromFragment) {
        this.context = context;
        this.list = list;
        this.fromFragment = fromFragment;
    }
    @NonNull
    @Override
    public UsersListAdminAdapter.UserListAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_users, parent, false);

        return new UsersListAdminAdapter.UserListAdminViewHolder(v);
    }
    //TODO: Список курсов у пользователя
    @Override
    public void onBindViewHolder(@NonNull UsersListAdminAdapter.UserListAdminViewHolder holder, int position) {


        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());

        String userID = user.getId();

        holder.storageReference.child("userprofile/" + userID).getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Glide.with(context)
                                .load(uri)
                                .into(holder.imageView)
                )
                .addOnFailureListener(e -> {
                });


        holder.cardView.setOnClickListener(view -> {

            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("key", userID);
            intent.putExtra("fromFragment", fromFragment);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserListAdminViewHolder extends RecyclerView.ViewHolder {

        TextView name, email;
        ImageView imageView;
        StorageReference storageReference;
        CardView cardView;
        DatabaseReference databaseReference;


        public UserListAdminViewHolder (@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_user_name);
            email = itemView.findViewById(R.id.tv_user_email);
            imageView = itemView.findViewById(R.id.imgView_course);
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            cardView = itemView.findViewById(R.id.cardViewElement);

        }
    }

}
