package com.assignment.moneytap.moneytap;

public class Person {
    String pageId;
    String title;
    String imageUrl;
    String description;

    public Person(String pagesid, String title, String imageUrl, String description) {
        this.pageId = pagesid;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getPageId() {
        return pageId;
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
