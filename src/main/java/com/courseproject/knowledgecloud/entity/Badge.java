package com.courseproject.knowledgecloud.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by qqq on 2/11/2016.
 */
@javax.persistence.Entity
@Table(name = "badge")
public class Badge implements Entity, Serializable {

    private Integer badgeId;
    private String name;
    private String desc;
    private Set<User> users = new HashSet<User>(0);

    public Badge() {
    }

    public Badge(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Badge(String name, String desc, Set<User> users) {
        this.name = name;
        this.desc = desc;
        this.users = users;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "BADGE_ID", unique = true, nullable = false)
    public Integer getBadgeId() {
        return this.badgeId;
    }

    public void setBadgeId(Integer badgeId) {
        this.badgeId = badgeId;
    }

    @Column(name = "NAME", nullable = false, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "[DESC]", nullable = false)
    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "badges")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
