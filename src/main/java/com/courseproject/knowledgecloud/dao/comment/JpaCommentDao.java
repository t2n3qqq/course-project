package com.courseproject.knowledgecloud.dao.comment;

import com.courseproject.knowledgecloud.dao.DataBaseInitializer;
import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.entity.*;
import com.courseproject.knowledgecloud.rest.resources.UserResource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by qqq on 2/12/2016.
 */
public class JpaCommentDao extends JpaDao<Comment, Integer> implements CommentDao{
    JpaCommentDao(){super(Comment.class);}

    @Override
    @Transactional
    public Comment assambleComment(Comment comment) {


        Comment comm = new Comment();
        User user = DataBaseInitializer.userDao.findByName(comment.username);
        Article article = DataBaseInitializer.articleDao.find(comment.articleId);
        comm.setArticle(article);
        comm.setContent(comment.getContent());
        comm.setDate(new Date(System.currentTimeMillis()));
        comm.setLikesnumber(0);
        comm.setUser(user);
        comm.username = comment.username;
        comm = DataBaseInitializer.commentDao.save(comm);
        comm.username = comment.username;
        return comm;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll(Comment comment){

        Article article = DataBaseInitializer.articleDao.find(comment.articleId);

        List<Comment> comments = new ArrayList<Comment>();
        comments.addAll(article.getComments());
        for(Comment com : comments){
            com.username = com.getUser().getName();
        }


        return comments;
    }
}
