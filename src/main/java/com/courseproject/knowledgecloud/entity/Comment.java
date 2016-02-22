package com.courseproject.knowledgecloud.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/12/2016.
 */
@javax.persistence.Entity
@Table(name = "comment")
public class Comment implements Entity, Serializable {
    private Integer commentId;
    private Date date;
    private String content;
    private Integer likesnumber;
    private Article article;
    private User user;

    public Comment() {
    }

    public Comment(String content) {
        this.date = new Date();
        this.content = content;
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "COMMENT_ID", unique = true, nullable = false)
    public Integer getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Column(name = "DATE", nullable = false, length = 10)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "[CONTENT]", nullable = false)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "LIKES_NUMBER", nullable = true)
    public Integer getLikesnumber(){return this.likesnumber;}

    public void setLikesnumber(Integer likesnumber){this.likesnumber = likesnumber;}

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)//(fetch = FetchType.LAZY)
    //@JoinColumn(name = "ARTICLE_ID", nullable = false)
    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article){
        this.article = article;
    }

    @JsonIgnore
    @ManyToOne//(fetch = FetchType.LAZY)
    //@JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    @Transient
    public String username;

    @Transient
    public Integer articleId;

}
