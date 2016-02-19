package com.courseproject.knowledgecloud.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/11/2016.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@javax.persistence.Entity
@Table(name = "article")
public class Article implements com.courseproject.knowledgecloud.entity.Entity, Serializable{
    private Integer articleId;
    private String name;
    private String content;
    private User user;
    private Set<CommentTable> commentTables = new HashSet<CommentTable>();
    private Set<Topic> topics = new HashSet<Topic>();

    public Article() {
    }

    public Article(String name, String content) {
        this.name = name;
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
    @Column(name = "ARTICLE_ID", unique = true, nullable = false)
    public Integer getArticleId() {
        return this.articleId;
    }

    public void setArticleId(Integer badgeId) {
        this.articleId = badgeId;
    }

    @Column(name = "NAME", nullable = false, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "[CONTENT]", nullable = false)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")//(fetch = FetchType.LAZY, mappedBy = "user")
    //@JoinColumn(name = "COMMENT_ID")
    public Set<CommentTable> getCommentTables() {
        return this.commentTables;
    }

    public void setCommentTables(Set<CommentTable> commentTables) {
        this.commentTables = commentTables;
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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "article_topic", joinColumns = {
            @JoinColumn(name = "ARTICLE_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "TOPIC_ID",
                    nullable = false, updatable = false) })
    public Set<Topic> getTopics(){
        return this.topics;
    }

    public void setTopics(Set<Topic> topics){
        this.topics = topics;
    }

    @Override
    public String toString(){
        return String.format("Article[%d,%s,%s]", this.articleId, this.name, this.content);
    }
}