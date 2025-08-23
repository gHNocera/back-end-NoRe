package com.ghartmann.dto;

import java.util.List;

public class PostDTO {
    private Long id;
    private String username;
    private Long timestamp;
    private String content;
    private int likes;
    private int comments;
    private List<String> likesBy;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getComments() { return comments; }
    public void setComments(int comments) { this.comments = comments; }

    public List<String> getLikesBy() { return likesBy; }
    public void setLikesBy(List<String> likesBy) { this.likesBy = likesBy; }
}
