package com.courseproject.knowledgecloud.dao.tag;

import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.entity.Tag;

/**
 * Created by qqq on 2/20/2016.
 */
public class JpaTagDao extends JpaDao<Tag, Integer> implements TagDao{
    JpaTagDao(){super(Tag.class);}
}
