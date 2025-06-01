//models(Comment.java)
package com.example.instaclone.models;

public class Comment {
    private String username;
    private String commentText;

    public Comment() {
        // Firestore needs no-arg constructor
    }

    public Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}
