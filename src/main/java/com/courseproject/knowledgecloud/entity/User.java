package com.courseproject.knowledgecloud.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@javax.persistence.Entity
public class User implements Entity, UserDetails, Serializable
{



	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Integer id;

	@Column(unique = true, length = 16, nullable = false)
	private String name;

	@Column(length = 80, nullable = false)
	private String password;

	@Column(unique = false, length = 16, nullable = false)
	private String language;

	@Column(unique = false, length = 16, nullable = false)
	private String theme;

	@Column(unique = false, nullable = true)
	private String fullname;

	@Column(unique = false, nullable = true)
	private String avatar;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles = new HashSet<String>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_badge", joinColumns = {
			@JoinColumn(name = "USER_ID", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "BADGE_ID",
					nullable = false, updatable = false) })
	private Set<Badge> badges = new HashSet<Badge>(0);

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Article> articles = new HashSet<Article>(0);

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Comment> comments = new HashSet<Comment>(0);

	public User()
	{
		/* Reflection instantiation */
	}

	public User(String name, String passwordHash){
		this.name = name;
		this.password = passwordHash;
	}

	public User(String name, String passwordHash, Set<Badge> badges)
	{
		this.name = name;
		this.password = passwordHash;
		this.badges = badges;
	}


	public Integer getId()
	{
		return this.id;
	}


	public void setId(Integer id)
	{
		this.id = id;
	}


	public String getName()
	{
		return this.name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Set<String> getRoles()
	{
		return this.roles;
	}


	public void setRoles(Set<String> roles)
	{
		this.roles = roles;
	}


	public void addRole(String role)
	{
		this.roles.add(role);
	}

	public Set<Badge> getBadges() {
		return this.badges;
	}
	public void setBadges(Set<Badge> badges) {
		this.badges = badges;
	}


	public Set<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String getPassword()
	{
		return this.password;
	}


	public void setPassword(String password)
	{
		this.password = password;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		Set<String> roles = this.getRoles();

		if (roles == null) {
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}
	



	@Override
	public String getUsername()
	{
		return this.name;
	}


	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}


	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}


	@Override
	public boolean isEnabled()
	{
		return true;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}


	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}