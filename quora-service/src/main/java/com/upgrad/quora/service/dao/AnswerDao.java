package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
public class AnswerDao {


    @Autowired
    private EntityManager entityManager;

    //this method is used to create a Answer
    public AnswerEntity create(AnswerEntity answer){
        entityManager.persist(answer);
        return answer;
    }

    //this method is used to get the Answer by uuid
    public AnswerEntity getAnswerById(String uuid){
        try {
            return entityManager.createNamedQuery("answerById", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //this method is used to update the answer
    public void updateAnswer(AnswerEntity answer){
        entityManager.merge(answer);
    }

    //this method is used to delete the answer
    public void deleteAnswer(AnswerEntity answer){
        entityManager.remove(answer);
    }
}
