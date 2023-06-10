package com.example.arguideapp;

import java.util.List;

public class User {
    private String link;
    private String name;




    public User() {
    }



    public User(String link, String text)
    {

        this.link = link;
        this.name = text;

    }


    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }


}
