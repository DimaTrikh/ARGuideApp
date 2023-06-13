package com.example.arguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AchievementDetailActivity extends AppCompatActivity {

    private ImageView DetailImageNamePoster;
    private TextView TextViewTitle;
    private TextView ProgressBarText;
    private TextView TextStatus;
    private TextView TextDescription;
    private ProgressBar ProgressBar;
    private static final String EXTRA_ACHIEVEMENT = "achievement";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_detail);
        initViews();
        Achievement.GetAchievement achievement = (Achievement.GetAchievement) getIntent().getSerializableExtra(EXTRA_ACHIEVEMENT);
        Glide.with(this).load(achievement.getLink()).into(DetailImageNamePoster);
        TextViewTitle.setText(achievement.getAchievementName());
        TextDescription.setText(achievement.getAchievementDescription());
        if(achievement.getStatus())
        {
            TextStatus.setText("Выполнена!");
            TextStatus.setTextColor(Color.GREEN);
        }
        else
        {
            TextStatus.setText("Не выполнена!");
            TextStatus.setTextColor(Color.RED);
        }

        if (achievement.getAchievementType().equals("Object"))
        {
            if(achievement.getStatus())
            {
                ProgressBarText.setText("1/1");
                ProgressBar.setProgress(100);
            }
            else
            {
                ProgressBarText.setText("0/1");
                ProgressBar.setProgress(0);
            }

        }
        else if (achievement.getAchievementType().equals("manyObjects"))
        {
            String[] s = achievement.getFoundName().split("&&");

            ProgressBarText.setText(String.valueOf(achievement.getProgress()) + "/" + s.length);


            ProgressBar.setProgress((Integer) (achievement.getProgress() * 100/s.length ));
        }
        else if(achievement.getAchievementType().equals("number"))
        {
            ProgressBarText.setText(String.valueOf(achievement.getProgress()) + "/" + achievement.getFoundName());
            ProgressBar.setProgress((Integer)(achievement.getProgress() * 100 /Integer.parseInt(achievement.getFoundName())));
        }




    }

    private void initViews()
    {
        DetailImageNamePoster = findViewById(R.id.DetailImageNamePoster);
        TextViewTitle = findViewById(R.id.TextViewTitle);
        ProgressBarText = findViewById(R.id.ProgressBarText);
        TextStatus = findViewById(R.id.TextStatus);
        TextDescription = findViewById(R.id.TextDescription);
        ProgressBar = findViewById(R.id.ProgressBar);

    }

    public static Intent newIntent(Context context, Achievement.GetAchievement achievement)
    {
        Intent intent = new Intent(context, AchievementDetailActivity.class);
        intent.putExtra(EXTRA_ACHIEVEMENT, achievement);
        return intent;
    }



}