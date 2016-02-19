package com.courseproject.knowledgecloud.dao.user;

import com.courseproject.knowledgecloud.dao.Dao;
import com.courseproject.knowledgecloud.entity.User;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserDao extends Dao<User, Integer>, UserDetailsService
{

	User findByName(String name);

}