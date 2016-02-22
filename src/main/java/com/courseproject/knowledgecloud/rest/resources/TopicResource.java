package com.courseproject.knowledgecloud.rest.resources;

import com.courseproject.knowledgecloud.JsonViews;
import com.courseproject.knowledgecloud.dao.comment.CommentDao;
import com.courseproject.knowledgecloud.dao.topic.TopicDao;
import com.courseproject.knowledgecloud.entity.Comment;
import com.courseproject.knowledgecloud.entity.Topic;
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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by qqq on 2/21/2016.
 */
@Component
@Path("/topics")
public class TopicResource {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TopicDao topicDao;


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
        List<Topic> allEntries = this.topicDao.findAll();
        String str = viewWriter.writeValueAsString(allEntries);
        System.out.println(str);
        return viewWriter.writeValueAsString(allEntries);
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
