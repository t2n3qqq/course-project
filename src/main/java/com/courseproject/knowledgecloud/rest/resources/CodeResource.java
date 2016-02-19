package com.courseproject.knowledgecloud.rest.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;


import com.courseproject.knowledgecloud.JsonViews;


import com.courseproject.knowledgecloud.dao.article.ArticleDao;
import com.courseproject.knowledgecloud.entity.Article;
import com.courseproject.knowledgecloud.entity.User;
import com.owlike.genson.Genson;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
@Path("/news")
public class CodeResource
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ObjectMapper mapper;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String list() throws JsonGenerationException, JsonMappingException, IOException
    {
        this.logger.info("list()");

        ObjectWriter viewWriter;
        if (this.isAdmin()) {
            viewWriter = this.mapper.writerWithView(JsonViews.Admin.class);
        } else {
            viewWriter = this.mapper.writerWithView(JsonViews.User.class);
        }
        List<Article> allEntries = this.articleDao.findAll();
        String str = viewWriter.writeValueAsString(allEntries);
        System.out.println(str);
        return viewWriter.writeValueAsString(allEntries);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Article read(@PathParam("id") Integer id)
    {
        this.logger.info("read(id)");

        Article article = this.articleDao.find(id);
        if (article == null) {
            throw new WebApplicationException(404);
        }
        return article;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Article create(Article article)
    {

        this.logger.info("create(): " + article);

        return this.articleDao.save(article);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Article update(@PathParam("id") Integer id, Article newsEntry)
    {
        this.logger.info("update(): " + newsEntry);

        return this.articleDao.save(newsEntry);
    }


    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void delete(@PathParam("id") Integer id)
    {
        this.logger.info("delete(id)");

        this.articleDao.delete(id);
    }


    private boolean isAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.toString().equals("admin")) {
                return true;
            }
        }

        return false;
    }

}