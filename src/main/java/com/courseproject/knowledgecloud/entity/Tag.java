package com.courseproject.knowledgecloud.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/20/2016.
 */
@Entity
@Table(name = "tag")
public class Tag implements com.courseproject.knowledgecloud.entity.Entity{
    private Integer tagId;
    private String name;
    private Set<Article> articles = new HashSet<Article>(0);

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

//    public Topic(String name, String desc, Set<User> users) {
//        this.name = name;
//        this.users = users;
//    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TAG_ID", unique = true, nullable = false)
    public Integer getTagId() {
        return this.tagId;
    }

    public void setTagId(Integer topicId) {
        this.tagId = topicId;
    }

    @Column(name = "NAME", nullable = false, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

}
