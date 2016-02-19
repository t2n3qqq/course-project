package com.courseproject.knowledgecloud.dao.article;

import com.courseproject.knowledgecloud.dao.DataBaseInitializer;
import com.courseproject.knowledgecloud.dao.user.JpaUserDao;
import com.courseproject.knowledgecloud.dao.user.UserDao;
import com.courseproject.knowledgecloud.entity.Article;
import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.entity.User;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qqq on 2/11/2016.
 */
public class JpaArticleDao extends JpaDao<Article, Integer> implements ArticleDao{
    JpaArticleDao(){super(Article.class);}

    @Override
    @Transactional(readOnly = true)
    public Article save(Article article){
        User user = DataBaseInitializer.userDao.findByName(UserResource.currentUser);
        article.setUser(user);
        return article;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Article> findAll(){
       // final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        //final CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);

        User user = DataBaseInitializer.userDao.findByName(UserResource.currentUser);

        List<Article> articles = new ArrayList<Article>();
        articles.addAll(user.getArticles());

        return articles;


//        User user;
//        Session session = this.getEntityManager().unwrap(Session.class);
//        session.getSessionFactory().openSession();
//        //User.class.getClass().getPackage().getName();
//        user =  (User) session.get(User.class, 1);
//        System.out.println(user.getName());
//        List<Article> articles = new ArrayList<Article>();
//        articles.addAll(user.getArticles());
//
//        return articles;

//        Session session = this.getEntityManager().unwrap(Session.class);
//        session.getSessionFactory().openSession();
//        Criteria cr = session.createCriteria(User.class).add(Restrictions.eq("name", "admin"))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property("id"), "id"))
//                        //.add(Projections.property("name"), "name"))
//                .setResultTransformer(Transformers.aliasToBean(User.class));
//
//        List<User> list = cr.list();
//        Integer userId = list.get(0).getId();
//
//        cr = session.createCriteria(Article.class).add(Restrictions.eq("user", 1))
//                .setProjection(Projections.projectionList()
//                        .add(Projections.property("articleId"), "articleId")
//                        .add(Projections.property("name"), "name"))
//                .setResultTransformer(Transformers.aliasToBean(Article.class));
//
//        List<Article> articles = cr.list();


//        Root<User> root = criteriaQuery.from(User.class);
//        UserTransfer userTransfer;
//        UserResource userResource = new UserResource();
//        //userResource.getUser();
//        userTransfer = userResource.getUser();
//        //System.out.println(userTransfer.getName());
//        //criteriaQuery.multiselect(User)
//        criteriaQuery.select(root).where(builder.equal(root.get("name"), userTransfer.getName()));
//        //criteriaQuery.orderBy(builder.desc(root.get("date")));
//
//        TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
//        List<User> us = new ArrayList<User>();
//        us = typedQuery.getResultList();
//        List<Article> ar = new ArrayList<Article>();
//        return ar; //typedQuery.getResultList();
    }
}
