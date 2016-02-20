package com.courseproject.knowledgecloud.dao.tag;

import com.courseproject.knowledgecloud.dao.Dao;
import com.courseproject.knowledgecloud.entity.Tag;

/**
 * Created by qqq on 2/20/2016.
 */
public interface TagDao extends Dao<Tag, Integer> {
    Tag findByName(String name);
}
