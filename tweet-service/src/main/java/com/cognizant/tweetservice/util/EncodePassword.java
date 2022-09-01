package com.cognizant.tweetservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePassword {


//    public static void main(String[] args) {
//        registerStudent();
//    }

    /**
     * Any user can access this API - No Authentication required
     * @return
     */

    public static String registerStudent(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }



}
