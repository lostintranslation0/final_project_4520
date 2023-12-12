package com.example.finalproject.ui.blogs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment implements Serializable {
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public void addReply(Comment reply)
    {
        if (this.replies == null)
        {
            this.replies = new ArrayList<>();
        }
        this.replies.add(reply);
    }


    private int level;
    private String user;
    private String comment;
    private Date date;

    private List<Comment> replies;

    public Comment(String user, String comment, Date date, int level) {
        this.level = level;
        this.user = user;
        this.comment = comment;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public static List<Comment> flattenComments(List<Comment> comments) {
        List<Comment> flatList = new ArrayList<>();
        for (Comment comment : comments) {
            addCommentAndReplies(flatList, comment, 0);
        }
        return flatList;
    }

    private static void addCommentAndReplies(List<Comment> flatList, Comment comment, int level) {
        comment.setLevel(level);
        flatList.add(comment);
        if (comment.getReplies() != null) {
            for (Comment reply : comment.getReplies()) {
                addCommentAndReplies(flatList, reply, level + 1);
            }
        }
    }
}
