package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SigninService;
import com.upgrad.quora.service.business.SignoutService;
import com.upgrad.quora.service.business.SignupService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.quora.service.entity.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SignupService signupBusinessService;

    @Autowired
    private SigninService signinService;

    @Autowired
    private SignoutService signoutService;

    @PostMapping("/signup")
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

    @PostMapping(path = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        //decoding the username and password from the authorization RequestHeader
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        //passing the username and password to the signupBusinessService
        UserAuthEntity token = this.signinService.login(decodedArray[0], decodedArray[1]);
        UserEntity user = token.getUser();

        SigninResponse signinResponse=new SigninResponse().id((user.getUuid())).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("access-token", token.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,httpHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {

        UserAuthEntity token = signoutService.signout(authorization);
        UserEntity user = token.getUser();
        SignoutResponse signoutResponse=new SignoutResponse().id(user.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }

}
