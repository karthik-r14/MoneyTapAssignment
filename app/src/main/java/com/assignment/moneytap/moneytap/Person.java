package com.assignment.moneytap.moneytap;

public class Person {
    String pagesid;
    String title;
    String imageUrl;
    String description;

    public Person(String pagesid, String title, String imageUrl, String description) {
        this.pagesid = pagesid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getPagesid() {
        return pagesid;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
