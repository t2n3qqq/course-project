package com.courseproject.knowledgecloud.dao;

import java.util.List;

import com.courseproject.knowledgecloud.entity.Entity;


public interface Dao<T extends Entity, I>
{

	List<T> findAll();


	T find(I id);


	T save(T newsEntry);


	void delete(I id);

}