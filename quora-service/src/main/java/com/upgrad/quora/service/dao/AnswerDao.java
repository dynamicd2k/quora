package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class AnswerDao {


    @Autowired
    private EntityManager entityManager;

    //this method is used to create a Answer
    public AnswerEntity create(AnswerEntity answer){
        entityManager.persist(answer);
        return answer;
    }
}
