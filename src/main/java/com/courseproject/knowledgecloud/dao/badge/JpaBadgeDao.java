package com.courseproject.knowledgecloud.dao.badge;

import com.courseproject.knowledgecloud.entity.Badge;
import com.courseproject.knowledgecloud.dao.JpaDao;

/**
 * Created by qqq on 2/11/2016.
 */
public class JpaBadgeDao extends JpaDao<Badge,Integer> implements BadgeDao{
    JpaBadgeDao(){super(Badge.class);}
}
