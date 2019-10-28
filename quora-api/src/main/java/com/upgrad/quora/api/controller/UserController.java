package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupService;
import com.upgrad.quora.service.business.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.upgrad.quora.service.entity.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SignupService signupBusinessService;

    @RequestMapping(method= RequestMethod.POST, value = "/user/signup")
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws Exception {

        final UserEntity user = new UserEntity();
        user.setUuid(UUID.randomUUID().toString());
        user.setFirstName(signupUserRequest.getFirstName());
        user.setLastName(signupUserRequest.getLastName());
        user.setUsername(signupUserRequest.getUserName());
        user.setEmail(signupUserRequest.getEmailAddress());
        user.setPassword(signupUserRequest.getPassword());
        user.setDob(signupUserRequest.getDob());
        user.setAboutMe(signupUserRequest.getAboutMe());
        user.setContact(signupUserRequest.getContactNumber());
        user.setCountry(signupUserRequest.getCountry());
        user.setRole("nonadmin");
        user.setSalt("1234abc");

        final UserEntity createdUserEntity = this.signupBusinessService.signup(user);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity(HttpStatus.OK);
    }

}
