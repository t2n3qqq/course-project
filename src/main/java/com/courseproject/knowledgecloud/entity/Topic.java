package com.courseproject.knowledgecloud.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/14/2016.
 */
@javax.persistence.Entity
@Table(name = "topic")
public class Topic implements com.courseproject.knowledgecloud.entity.Entity {
    private Integer topicId;
    private String name;
    private Set<Article> articles = new HashSet<Article>(0);

    public Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "TOPIC_ID", unique = true, nullable = false)
    public Integer getTopicId() {
        return this.topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    @Column(name = "NAME", nullable = false, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "topics")
    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

}
