package com.courseproject.knowledgecloud.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;


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
    private Date date;
    private Integer vote;
    private User user;
    private Set<Comment> comments = new HashSet<Comment>();
    private Set<Topic> topics = new HashSet<Topic>();
    private Set<Tag> tags = new HashSet<Tag>();
    private String imgurl;

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

    @Column(name = "NAME", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "[CONTENT]", nullable = false)
    @Type(type = "text")
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "DATE", nullable = false)
    public Date getDate(){
        return this.date;
    }

    public void setDate(Date date){
        this.date = date;
    }


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")//(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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
            @JoinColumn(name = "ARTICLE_ID", nullable = false, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "TOPIC_ID",
                    nullable = false, updatable = false) })
    public Set<Topic> getTopics(){
        return this.topics;
    }

    public void setTopics(Set<Topic> topics){
        this.topics = topics;
    }

    @Transient
    public List<String> topicslist;

    @Transient
    public List<String> tagslist;

    @Transient
    public String thisuser;// = this.user.getName();

    @Override
    public String toString(){
        return String.format("Article[%d,%s,%s]", this.articleId, this.name, this.content);
    }

    @Column(name = "VOTE", nullable = true)
    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "article_tag", joinColumns = {
            @JoinColumn(name = "ARTICLE_ID", nullable = true, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "TAG_ID",
                    nullable = true, updatable = true) })
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Column(name = "IMGURL", nullable = true)
    @Type(type = "text")
    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
