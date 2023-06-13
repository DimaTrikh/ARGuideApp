package com.example.arguideapp;

import android.util.Log;
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

    private List<Achievement.GetAchievement> achievements = new ArrayList<>();

    private OnAchievementClickListener onAchievementClickListener;

    public void setAchievements(List<Achievement.GetAchievement> achievements)
    {
        this.achievements = achievements;
        notifyDataSetChanged();
    }


    public void setOnAchievementClickListener(OnAchievementClickListener onAchievementClickListener) {
        this.onAchievementClickListener = onAchievementClickListener;
    }

    @NonNull
    @Override
    public AchievementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new AchievementsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AchievementsViewHolder holder, int position) {
        //Achievement.GetAchievement achievement123 = new Achievement.GetAchievement("","","","","", false, 10);
//        Achievement achievement = achievements.get(position);
        Achievement.GetAchievement achievement = achievements.get(position);
        Log.e(achievement.getFoundName() + achievement.getAchievementType() + achievement.getAchievementName(), achievement.getLink() + achievement.getAchievementDescription());
        Glide.with(holder.itemView).load(achievement.getLink()).into(holder.imageViewPoster);
        holder.imageNamePoster.setText(achievement.getAchievementName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAchievementClickListener != null)
                {
                    onAchievementClickListener.onAchievementClick(achievement);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }


    interface OnAchievementClickListener
    {
        void onAchievementClick(Achievement.GetAchievement achievement);
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
