package com.example.arguideapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementsViewHolder> {

    private List<Achievement> achievements = new ArrayList<>();

    public void setAchievements(List<Achievement> achievements)
    {
        this.achievements = achievements;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new AchievementsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AchievementsViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        Glide.with(holder.itemView).load(achievement.getLink()).into(holder.imageViewPoster);
        holder.imageNamePoster.setText(achievement.getAchievementName());
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class AchievementsViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView imageViewPoster;
        private final TextView imageNamePoster;

        public AchievementsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            imageNamePoster = itemView.findViewById(R.id.imageNamePoster);
        }


    }
}
