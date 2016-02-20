package com.courseproject.knowledgecloud.dao.topic;

import com.courseproject.knowledgecloud.entity.Topic;
import com.courseproject.knowledgecloud.dao.JpaDao;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by qqq on 2/14/2016.
 */
public class JpaTopicDao extends JpaDao<Topic, Integer> implements TopicDao {
    JpaTopicDao(){super(Topic.class);}

    @Override
    @Transactional(readOnly = true)
    public Topic findByName(String name)
    {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Topic> criteriaQuery = builder.createQuery(this.entityClass);

        Root<Topic> root = criteriaQuery.from(this.entityClass);
        Path<String> namePath = root.get("name");
        criteriaQuery.where(builder.equal(namePath, name));

        TypedQuery<Topic> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        List<Topic> users = typedQuery.getResultList();

        if (users.isEmpty()) {
            return null;
        }

        return users.iterator().next();
    }

}
