package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @PostMapping(path = "question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") String questionId,
                                                       @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {

        final AnswerEntity answer = new AnswerEntity();
        answer.setUuid(UUID.randomUUID().toString());
        answer.setDate(ZonedDateTime.now());
        answer.setAnswer(answerRequest.getAnswer());
        AnswerEntity answerId = this.answerService.create(answer, authorization,questionId);

        AnswerResponse answerResponse=new AnswerResponse().id(answerId.getUuid()).status("ANSWER CREATED");
        return  new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @PutMapping(path ="answer/edit/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public  ResponseEntity<AnswerEditResponse> editAnswerContent(@RequestHeader("authorization") final String authorization,
                                                                 @PathVariable("answerId")String answerId, final AnswerEditRequest answerEditRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {

        AnswerEntity answer = new AnswerEntity();
        answer.setUuid(answerId);
        answer.setAnswer(answerEditRequest.getContent());

        AnswerEntity updatedAnswer = this.answerService.update(authorization, answer);

        AnswerEditResponse answerEditResponse=new AnswerEditResponse().id(updatedAnswer.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }

    @DeleteMapping(path ="answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") final String authorization,
                                                             @PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answer = answerService.delete(answerId,authorization);

        AnswerDeleteResponse answerDeleteResponse=new AnswerDeleteResponse().id(answer.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }
    @GetMapping(path = "answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswersToQuestion(@RequestHeader("authorization") final String authorization,
                                                                         @PathVariable("questionId")String questionId)
            throws AuthorizationFailedException,InvalidQuestionException{

        List<AnswerEntity> answers = answerService.getAnswers(authorization,questionId);
        String content = " ";
        String id = " ";
        String[] ans = new String[2];
        for(AnswerEntity q : answers){
            content +=  q.getAnswer() + " , ";
            id +=  q.getUuid() + " , " ;
        }
        ans[0]=id;
        ans[1]=content;
        String question =answers.get(0).getQuestion().getContent();
        AnswerDetailsResponse answerDetailsResponse=new AnswerDetailsResponse().id(ans[0]).
                questionContent(question).answerContent(ans[1]);
        return new ResponseEntity<AnswerDetailsResponse>(answerDetailsResponse,HttpStatus.OK);
    }

}
