package com.cts.authorization.service;

import com.cts.authorization.exception.InvalidCredentialsException;
import com.cts.authorization.exception.TweetAppException;
import com.cts.authorization.model.UserData;
import com.cts.authorization.repository.UserRepo;
import com.cts.authorization.util.Constants;
import com.cts.authorization.util.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String USER_DOES_NOT_EXISTS_MESSAGE = "user not found";
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info(Constants.IN_REQUEST_LOG, "UserDetailsServiceImpl",userName);
        Optional<UserData> userPresent = userRepo.findByEmailIdName(userName);
        if (userPresent.isEmpty()) {
            System.out.println("In exception");
            throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, userPresent.toString());
        }
        log.info(Constants.EXITING_RESPONSE_LOG, "username", userPresent.isEmpty() ? "Not Present" : "Present");
        //log.info(Constants.EXITING_RESPONSE_LOG, "login", userPresent);
        return new User(userPresent.get().getEmail(), userPresent.get().getPassword(), new ArrayList<>());
    }
}
