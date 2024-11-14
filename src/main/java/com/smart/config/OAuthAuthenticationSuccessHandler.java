package com.smart.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.smart.Entities.User;
import com.smart.dao.UserRepository;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;
    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2)
            throws IOException, ServletException {
       
        // logger.info("OAuthAuthenticationSuccessHandler");
        // // arg1.sendRedirect("/home");
        DefaultOAuth2User user= (DefaultOAuth2User)arg2.getPrincipal();

        // logger.info(user.getName());
        // user.getAttributes().forEach((key, value)->
        // {
        //     logger.info("{} : {}",key, value);
        // });
        // logger.info(user.getAuthorities().toString());
        String email= user.getAttribute("email").toString();
        String name=user.getAttribute("name").toString();
        String picture=user.getAttribute("picture").toString();

        User user2=new User();
        user2.setEmail(email);
        user2.setImageUrl(picture);
        user2.setName(name);
        user2.setPassword("Password");
        user2.setId(Integer.parseInt(UUID.randomUUID().toString()));
        user2.setEnabled(true);
        user2.setAbout("This is : "+name);
        user2.setRole("ROLE_USER");

        User user3=userRepository.loadUserByUsername(email);
        if(user3==null)
        {
            userRepository.save(user2);
            logger.info("user saved"+ email);
        }


        new DefaultRedirectStrategy().sendRedirect(arg0, arg1, "/user/index");
        
    }

    
}
