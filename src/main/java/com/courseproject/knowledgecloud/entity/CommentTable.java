package com.courseproject.knowledgecloud.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/12/2016.
 */
@javax.persistence.Entity
@Table(name = "comment")
public class CommentTable implements Entity, Serializable {
    private Integer commentId;
    private Date date;
    private String content;
    private Integer likesnumber;
    private Article article;

    public CommentTable() {
    }

    public CommentTable(String content) {
        this.date = new Date();
        this.content = content;
        //this.user = user;
    }

//    public Article(String name, String desc, Set<User> users) {
//        this.name = name;
//        this.content = desc;
//        this.users = users;
//    }

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

    @ManyToOne(cascade = CascadeType.ALL)//(fetch = FetchType.LAZY)
    //@JoinColumn(name = "ARTICLE_ID", nullable = false)
    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article){
        this.article = article;
    }



}
