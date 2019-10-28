package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    //this method is used to delete the question
    public void deleteQuestion(QuestionEntity question){
        entityManager.remove(question);
    }

    //this method is used to get the Question by uuid
    public QuestionEntity getQuestionById(String uuid){
        try {
            return entityManager.createNamedQuery("questionById", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
