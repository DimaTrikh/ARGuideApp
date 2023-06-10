package com.example.arguideapp;

public class Achievement {
    private String achievementName;
    private String achievementDescription;

    private String foundName;

    private Boolean status;

    public Achievement() {
    }

    public Achievement(String foundName, String achievementName, String achievementDescription, Boolean status) {

        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.status = status;
        this.foundName = foundName;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public String getFoundName() {
        return foundName;
    }

    public Boolean getStatus() {
        return status;
    }
}
