package com.courseproject.knowledgecloud.dao.comment;

import com.courseproject.knowledgecloud.dao.Dao;
import com.courseproject.knowledgecloud.entity.Comment;

import java.util.List;

/**
 * Created by qqq on 2/12/2016.
 */
public interface CommentDao extends Dao<Comment, Integer> {
    Comment assambleComment (Comment comment);
    List<Comment> findAll (Comment comment);
}
