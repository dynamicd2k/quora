package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @PostMapping( path = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        final QuestionEntity question = new QuestionEntity();
        question.setUuid(UUID.randomUUID().toString());
        question.setContent(questionRequest.getContent());
        question.setDate(ZonedDateTime.now());
        QuestionEntity questionId = this.questionService.create(authorization, question);

        QuestionResponse questionResponse = new QuestionResponse().id(questionId.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @GetMapping( path = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        String[] questions = questionService.getQuestions(authorization);

        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(questions[0]).content(questions[1]);
        return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse, HttpStatus.OK);
    }
    @DeleteMapping(path = "/delete/{questionId}",produces =MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization,
                                                                 @PathVariable("questionId") String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity question = questionService.deleteQuestion(questionId, authorization);

        QuestionDeleteResponse questionDeleteResponse=new QuestionDeleteResponse().id(question.getUuid()).status("QUESTION DELETED");
        return  new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
    }

    @PutMapping(path = "/question/edit/{questionId}",produces =MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest,
                                                                    @RequestHeader("authorization") final String authorization,
                                                                    @PathVariable("questionId") String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity editQuestion = new QuestionEntity();
        editQuestion.setUuid(questionId);
        editQuestion.setContent(questionEditRequest.getContent());

        QuestionEntity question = questionService.editQuestion(authorization, editQuestion);

        QuestionEditResponse questionEditResponse=new QuestionEditResponse().id(question.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse,HttpStatus.OK);
    }
}
