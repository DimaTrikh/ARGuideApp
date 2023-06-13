package com.example.arguideapp;

public class UserAchievementObjs extends UserAchievement {
    private Integer progress;

    public UserAchievementObjs() { }

    public UserAchievementObjs(Boolean status, Integer progress)
    {
        super(status);
        this.progress = progress;
    }


    public Integer getProgress()
    {
        return progress;
    }

}
