package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class CommonController {

    @Autowired
    UserProfileService userProfileService;

    @RequestMapping(method = RequestMethod.GET,path = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") String id ,
                                                           @RequestHeader("authorization")final String authorization)
            throws UserNotFoundException, AuthorizationFailedException {

        UserEntity user = this.userProfileService.details(id, authorization);

        UserDetailsResponse userDetailsResponse=new UserDetailsResponse().firstName(user.getFirstName()).lastName(user.getLastName())
                .userName(user.getUsername()).emailAddress(user.getEmail()).dob(user.getDob()).
                        country(user.getCountry()).aboutMe(user.getAboutMe()).contactNumber(user.getContact());

        return  new ResponseEntity<UserDetailsResponse>( userDetailsResponse, HttpStatus.OK);
    }
}
