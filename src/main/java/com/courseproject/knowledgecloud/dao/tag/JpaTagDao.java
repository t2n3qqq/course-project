package com.courseproject.knowledgecloud.dao.tag;

import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by qqq on 2/20/2016.
 */
public class JpaTagDao extends JpaDao<Tag, Integer> implements TagDao{
    JpaTagDao(){super(Tag.class);}

    @Override
    @Transactional(readOnly = true)
    public Tag findByName(String name)
    {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Tag> criteriaQuery = builder.createQuery(this.entityClass);

        Root<Tag> root = criteriaQuery.from(this.entityClass);
        Path<String> namePath = root.get("name");
        criteriaQuery.where(builder.equal(namePath, name));

        TypedQuery<Tag> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        List<Tag> users = typedQuery.getResultList();

        if (users.isEmpty()) {
            return null;
        }

        return users.iterator().next();
    }
}
