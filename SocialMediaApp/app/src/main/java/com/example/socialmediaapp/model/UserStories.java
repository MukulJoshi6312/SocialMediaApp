package com.example.socialmediaapp.model;

public class UserStories {

    private String Image;
    private long storyAt;

    public UserStories(String image, long storyAt) {
        Image = image;
        this.storyAt = storyAt;
    }

    public UserStories() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }
}
