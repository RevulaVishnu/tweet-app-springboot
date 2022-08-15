package com.cts.authorization.service;

import com.cts.authorization.exception.TweetAppException;
import com.cts.authorization.model.UserData;
import com.cts.authorization.repository.UserRepository;
import com.cts.authorization.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info(Constants.IN_REQUEST_LOG, "In UserDetailsServiceImpl",userName);
        Optional<UserData> userOp = userRepository.findById(userName);
        System.out.println(userOp.toString());
        System.out.println(userOp.get().getEmail()+" | "+ userOp.get().getPassword());
        return new User(userOp.get().getEmail(), userOp.get().getPassword(), new ArrayList<>());
    }
}
