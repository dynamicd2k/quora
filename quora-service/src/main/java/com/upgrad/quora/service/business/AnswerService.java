package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AnswerService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    UserDao userDao;

    @Autowired
    AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity create(AnswerEntity answer, String authorizationToken, String questionId) throws AuthorizationFailedException,
            InvalidQuestionException {

        QuestionEntity question= questionDao.getQuestionById(questionId);
        if(question==null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        //else the user and the question of the answer is set and saved in the database
        answer.setQuestion(question);
        UserEntity user = token.getUser();
        answer.setUser(user);
        AnswerEntity answerId = answerDao.create(answer);

        return answerId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity update(String authorizationToken,AnswerEntity editAnswer)throws AuthorizationFailedException,
            AnswerNotFoundException {

        UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        AnswerEntity answer = answerDao.getAnswerById(editAnswer.getUuid());
        //if answer Does not exist in the database,AnswerNotFoundException is thrown
        if (answer==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        UserEntity user = token.getUser();
        //if the owner of the answer is logged in user then the answer is updated in the database
        if(answer.getUser() == user)
        {
            answer.setAnswer(editAnswer.getAnswer());
            answer.setDate(ZonedDateTime.now());
            answerDao.updateAnswer(answer);
            return answer;
        }
        //else AuthorizationFailedException is thrown
        throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity delete(String id,String authorizationToken )throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
        //if the access token is not there in the database, AuthorizationFailedException is thrown
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the access token is valid but the user has not logged in, AuthorizationFailedException is thrown
        if(token.getLogoutAt()!= null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        UserEntity user =token.getUser();

        AnswerEntity answer = answerDao.getAnswerById(id);
        //if answer Does not exist in the database,AnswerNotFoundException is thrown
        if (answer==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        //if the user of the answer is the logged in user or the role of the user is admin the question is deleted from the database
        if(answer.getUser() == user || user.getRole().equals("admin")){
            answerDao.deleteAnswer(answer);
            return answer;
        }
        //else AuthorizationFailedException is thrown
        throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
    }
}
