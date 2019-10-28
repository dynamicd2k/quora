package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    //this method is used to create a user in the database
    public UserEntity createUser(UserEntity user) {
        entityManager.persist(user);
        return user;
    }

    //this method is used to get a user by email from the database
    public UserEntity getUserByEmail(final String email) {
        try {
            return this.entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //this method is used to get a user by username from the database
    public UserEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //this method is used to get a user by uuid from the database
    public UserEntity getUserById(final String uuid){
        try {
        return entityManager.createNamedQuery("userByid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    //this method is used to delete a user from the database
    public UserEntity deleteUser(final String uuid){
        try {
            UserEntity user=entityManager.createNamedQuery("userByid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();
            entityManager.remove(user);
            return user;
        }
        catch (NoResultException nre){
            return null;
        }
    }

    //this method is used to create a UserAuthToken in the Database
    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    //this method is used to get a UserAuthToken from the database
    public UserAuthEntity getUserAuthToken(final String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
