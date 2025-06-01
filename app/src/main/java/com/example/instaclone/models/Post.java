//models(Post.java)
package com.example.instaclone.models;

public class Post {
    private String postId;
    private String imageUrl;
    private String username;
    private String description;
    private Integer likeCount;

    // Required empty constructor for Firebase
    public Post() {
    }

    public Post(String postId, String imageUrl, String username, String description, Integer likeCount) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.username = username;
        this.description = description;
        this.likeCount = likeCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
