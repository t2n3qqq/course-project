package com.courseproject.knowledgecloud.dao.topic;

import com.courseproject.knowledgecloud.entity.Topic;
import com.courseproject.knowledgecloud.dao.JpaDao;

/**
 * Created by qqq on 2/14/2016.
 */
public class JpaTopicDao extends JpaDao<Topic, Integer> implements TopicDao {
    JpaTopicDao(){super(Topic.class);}
}
