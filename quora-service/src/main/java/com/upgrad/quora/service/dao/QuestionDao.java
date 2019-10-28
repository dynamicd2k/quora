package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class QuestionDao {
    @PersistenceContext
    private EntityManager entityManager;

    //this method is used to create a Question
    public QuestionEntity create(QuestionEntity question){
        entityManager.persist(question);
        return question;
    }
    //this method is used to get a list of all the Questions
    public List<QuestionEntity> getQuestions(){
        TypedQuery<QuestionEntity> query =entityManager.createQuery("SELECT p from questionEntity p", QuestionEntity.class);
        List<QuestionEntity> questions = query.getResultList();
        return questions;
    }
}
