package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity create(String authorizationToken, QuestionEntity question)throws AuthorizationFailedException {

        UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        //else the question is created and saved in the database
        UserEntity user = token.getUser();
        question.setUser(user);
        QuestionEntity questionId = questionDao.create(question);

        return questionId;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public String [] getQuestions(String authorizationToken)throws AuthorizationFailedException{

        UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        //else the list of all the questions in the database is returned to the controller
        List<QuestionEntity> questions= questionDao.getQuestions();
        String content = " ";
        String id = " ";
        String[] question = new String[2];
        for(QuestionEntity q : questions){
            content +=  q.getContent() + " , ";
            id +=  q.getUuid() + " , " ;
        }
        question[0]=id;
        question[1]=content;
        return question;
    }
}
