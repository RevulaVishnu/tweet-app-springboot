package com.cts.authorization.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePassword {


    public static void main(String[] args) {
        registerStudent();
    }

    /**
     * Any user can access this API - No Authentication required
     * @return
     */

    public static void registerStudent() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("vishnu"));
//        String en1 = bCryptPasswordEncoder.encode("password1");
//        String en2 = bCryptPasswordEncoder.encode("password1");

//        if(bCryptPasswordEncoder.matches(en1,en2)){
//            System.out.println("Matched");
//        }
    }



}
