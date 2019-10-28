package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {
    @PersistenceContext
    private EntityManager entityManager;

    //this method is used to create a Question
    public QuestionEntity create(QuestionEntity question){
        entityManager.persist(question);
        return question;
    }
}
