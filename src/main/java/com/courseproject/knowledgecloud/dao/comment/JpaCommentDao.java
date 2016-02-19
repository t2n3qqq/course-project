package com.courseproject.knowledgecloud.dao.comment;

import com.courseproject.knowledgecloud.dao.JpaDao;
import com.courseproject.knowledgecloud.entity.CommentTable;

/**
 * Created by qqq on 2/12/2016.
 */
public class JpaCommentDao extends JpaDao<CommentTable, Integer> implements CommentDao{
    JpaCommentDao(){super(CommentTable.class);}
}
