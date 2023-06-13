package com.example.arguideapp;

public class Achievement {
    private String achievementName;
    private String achievementDescription;

    private String foundName;

    private String achievementType;

    private String link;

    public Achievement() {
    }

//    public Achievement(String achievementType, String achievementName, String achievementDescription)
//    {
//        this.achievementType = achievementType;
//        this.achievementDescription = achievementDescription;
//        this.achievementName = achievementName;
//        this.foundName = "";
//
//    }
    public Achievement(String achievementName, String link) {

    this.achievementName = achievementName;
    this.link = link;
    }
    public Achievement(String achievementName, String achievementDescription, String link) {

        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.link = link;
    }

    public Achievement(String achievementType, String foundName, String achievementName, String achievementDescription) {

        this.achievementName = achievementName;
        this.achievementDescription = achievementDescription;
        this.achievementType = achievementType;
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

    public String getAchievementType() {
        return achievementType;
    }

    public String getLink(){return link;}
}
