package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.*;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity user) throws SignUpRestrictedException {

        final UserEntity userByEmail = this.userDao.getUserByEmail(user.getEmail());
        final UserEntity userByUsername = this.userDao.getUserByUsername(user.getUsername());
        //if user with the respective username is already present in the database,AuthenticationFailedException exception is thrown
        if(userByUsername !=null)
        {
            throw new SignUpRestrictedException("SGR-001","Try any other Username,this Username has already been taken.");
        }
        //if user with the respective email is already present in the database,AuthenticationFailedException exception is thrown
        else if(userByEmail != null)
        {
            throw new SignUpRestrictedException("SGR-002","This user has already been registered,try with any other emailId");
        }
        //else the password of the user is encrypted and the user is registered in the database
        else {
            String[] encryptedText = passwordCryptographyProvider.encrypt(user.getPassword());
            user.setSalt(encryptedText[0]);
            user.setPassword(encryptedText[1]);
            return this.userDao.createUser(user);
        }
    }
}
