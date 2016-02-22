package com.courseproject.knowledgecloud.dao;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.courseproject.knowledgecloud.dao.article.ArticleDao;
import com.courseproject.knowledgecloud.dao.tag.TagDao;
import com.courseproject.knowledgecloud.entity.*;
import com.courseproject.knowledgecloud.dao.badge.BadgeDao;
import com.courseproject.knowledgecloud.dao.comment.CommentDao;
import com.courseproject.knowledgecloud.dao.topic.TopicDao;
import com.courseproject.knowledgecloud.dao.user.UserDao;

import org.springframework.security.crypto.password.PasswordEncoder;


public class DataBaseInitializer
{

	public static UserDao userDao;

	public static PasswordEncoder passwordEncoder;

	public static BadgeDao badgeDao;

	public static ArticleDao articleDao;

	public static CommentDao commentDao;

	public static TopicDao topicDao;

	public static TagDao tagDao;


	protected DataBaseInitializer()
	{
		/* Default constructor for reflection instantiation */
	}


	public DataBaseInitializer(UserDao userDao, PasswordEncoder passwordEncoder,
							   BadgeDao badgeDao, ArticleDao articleDao, CommentDao commentDao
								, TopicDao topicDao, TagDao tagDao)
	{
		this.tagDao = tagDao;
		this.topicDao = topicDao;
		this.commentDao = commentDao;
		this.articleDao = articleDao;
		this.userDao = userDao;
		this.badgeDao = badgeDao;
		this.passwordEncoder = passwordEncoder;
	}


	public void initDataBase()
	{

		Badge badge = new Badge("BADGE_1","DESC1");
		Badge badge1 = new Badge("BADGE_2","DESC2");
		Set<Badge> badges = new HashSet<Badge>();
		badges.add(badge);
		badges.add(badge1);
		User adminUser = new User("admin", this.passwordEncoder.encode("admin"), badges);
		adminUser.addRole("admin");
		adminUser.addRole("user");
		adminUser.setTheme("light");
		adminUser.setLanguage("eng");

//		User user = new User("test", this.passwordEncoder.encode("test"), badges);
//		adminUser.addRole("admin");
//		adminUser.addRole("user");

		//this.userDao.save(adminUser);

		Article article = new Article("Art_1", "TEST CONTETNT 1");
		Article article1 = new Article("Art_2", "TEST CONTETNT 2");
//		Article article2 = new Article("Art_3", "TEST CONTETNT 3");

		article.setUser(adminUser);//article1.setUser(adminUser);//article2.setUser(user);

		Comment comment = new Comment("TEST COMMENT 1");
		Comment comment1 = new Comment("TEST COMMENT 2");
		Comment comment2 = new Comment("TEST COMMENT 3");
		comment.setArticle(article);
		comment1.setArticle(article);
		comment.setUser(adminUser);
		comment1.setUser(adminUser);
		 //commentTable1.setArticle(article);//commentTable2.setArticle(article2);
		Set<Comment> comments = new HashSet<Comment>();
		Set<Comment> commentTables1 = new HashSet<Comment>();
		comments.add(comment);
		comments.add(comment1);
		commentTables1.add(comment2);

		article.setComments(comments);
		article.setImgurl("/Public/adasd");
		article1.setComments(commentTables1);

		Topic topic1 = new Topic();
		Topic topic2 = new Topic();
		topic1.setName("CHEMISTRY");
		topic2.setName("PHYSICS");
		Tag tag1 = new Tag();
		Tag tag2 = new Tag();
		tag1.setName("SWAG");
		tag2.setName("BITCH");
		Set<Tag> tags = new HashSet<Tag>();
		tags.add(tag1);tags.add(tag2);

		Set<Topic> topics = new HashSet<Topic>();
		Set<Topic> topics1 = new HashSet<Topic>();
		Set<Topic> topics2 = new HashSet<Topic>();
		topics.add(topic1);
		topics.add(topic2);
		topics1.add(topic1);
		topics2.add(topic2);

		article.setTopics(topics);
		article.setTags(tags);
		article.setDate(new Date(System.currentTimeMillis()));
	//	article1.setTopics(topics1);
//		article2.setTopics(topics2);

		Set<Article> articles = new HashSet<Article>();
	//	Set<Article> articles1 = new HashSet<Article>();
		articles.add(article);
	//	articles.add(article1);
	//	articles1.add(article2);

		adminUser.setArticles(articles);
		adminUser.setComments(comments);
	//	user.setArticles(articles1);
		//adminUser.getArticles().add(article);
		//this.articleDao.save(article);
		this.userDao.save(adminUser);
//		this.userDao.save(user);
		//this.articleDao.save(article);

		//this.userDao.save(adminUser);

		long timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24;
		for (int i = 0; i < 2; i++) {
//			Code code = new Code();
//			code.setTitle("Test " + i);
//			code.setContent("This is example content " + i);
//			code.setDate(new Date(timestamp));
//			code.setUserId("admin");
//			this.codeDao.save(code);
			timestamp += 1000 * 60 * 60;
		}
		//System.out.println("Hibernate many to many (Annotation)");
		//Session session = HibernateUtil.getSessionFactory().openSession();

		//session.beginTransaction();





		//this.stockDao.getTransaction().commit();
		//System.out.println("Done");

	}

}

