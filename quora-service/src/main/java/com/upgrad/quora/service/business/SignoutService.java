package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignoutService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String authorizationToken) throws SignOutRestrictedException {

       UserAuthEntity token = userDao.getUserAuthToken(authorizationToken);
       //if the access token is not there in the database, SignOutRestrictedException is thrown
       if (token == null){
           throw new SignOutRestrictedException("SGR-001","User is not Signed in");
       }
       //if the access token is valid then the logout time is updated and the token is returned
       else
           token.setLogoutAt(ZonedDateTime.now());

       return token;
    }
}
