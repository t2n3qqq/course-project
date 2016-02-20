package com.courseproject.knowledgecloud.dao.article;

import com.courseproject.knowledgecloud.dao.Dao;
import com.courseproject.knowledgecloud.entity.Article;

import java.util.List;

/**
 * Created by qqq on 2/11/2016.
 */
public interface ArticleDao extends Dao<Article, Integer> {
    Article assambleArticle (Article article, List<String> topicslist, List<String> tagslist);
}
