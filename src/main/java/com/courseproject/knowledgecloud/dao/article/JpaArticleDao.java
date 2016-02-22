package com.courseproject.knowledgecloud.dao.article;

import com.courseproject.knowledgecloud.dao.DataBaseInitializer;
import com.courseproject.knowledgecloud.dao.user.JpaUserDao;
import com.courseproject.knowledgecloud.dao.user.UserDao;
import com.courseproject.knowledgecloud.entity.*;
import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.rest.resources.UserResource;
import com.courseproject.knowledgecloud.transfer.UserTransfer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transaction;
import java.util.*;

/**
 * Created by qqq on 2/11/2016.
 */
public class JpaArticleDao extends JpaDao<Article, Integer> implements ArticleDao {
    JpaArticleDao() {
        super(Article.class);
    }

    @Override
    @Transactional
    public Article assambleArticle(Article article, List<String> topicslist, List<String> tagslist) {

        Set<Topic> topics = new HashSet<Topic>();
        for (String topic : topicslist) {
            Topic newtopic = DataBaseInitializer.topicDao.findByName(topic);
            topics.add(newtopic);
        }
        Set<Tag> tags = new HashSet<Tag>();
        for (String tag : tagslist) {
            Tag newtag = DataBaseInitializer.tagDao.findByName(tag);
            tags.add(newtag);
        }

        User user = DataBaseInitializer.userDao.findByName(UserResource.currentUser);
        article.setTopics(topics);
        article.setTags(tags);
        article.setUser(user);
        article.setDate(new Date(System.currentTimeMillis()));
        DataBaseInitializer.articleDao.save(article);
        return article;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> findAll() {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Article> criteriaQuery = builder.createQuery(Article.class);

        Root<Article> root = criteriaQuery.from(Article.class);
        criteriaQuery.select(root);//.where(builder.equal(root.get("userId"), UserResource.currentUser));
        criteriaQuery.orderBy(builder.desc(root.get("date")));

        TypedQuery<Article> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        List<Article> articles = typedQuery.getResultList();
        for (Article art : articles) {
            art.topicslist = new ArrayList<String>();
            art.tagslist = new ArrayList<String>();
            for (Topic top : art.getTopics()) {
                art.topicslist.add(top.getName());
            }
            for (Tag tag : art.getTags()) {
                art.tagslist.add(tag.getName());
            }
            art.thisuser = art.getUser().getName();

        }
        return typedQuery.getResultList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Article> findAllforUser() {
        User user = DataBaseInitializer.userDao.findByName(UserResource.currentUser);
        List<Article> articles = new ArrayList<Article>();
        articles.addAll(user.getArticles());
        for (Article art : articles) {
            art.topicslist = new ArrayList<String>();
            art.tagslist = new ArrayList<String>();
            for (Topic top : art.getTopics()) {
                art.topicslist.add(top.getName());
            }
            for (Tag tag : art.getTags()) {
                art.tagslist.add(tag.getName());
            }
            art.thisuser = art.getUser().getName();

        }

        return articles;
    }
}