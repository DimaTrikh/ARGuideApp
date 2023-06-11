package com.example.arguideapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<User> users = new ArrayList<>();

    public void setUser(List<User> user) {
        this.users = user;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        User user = users.get(position);
        Glide.with(holder.itemView).load(user.getLink()).into(holder.imageViewPoster);
        holder.imageNamePoster.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageViewPoster;
        private TextView imageNamePoster;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            imageNamePoster = itemView.findViewById(R.id.imageNamePoster);
        }


    }
}
