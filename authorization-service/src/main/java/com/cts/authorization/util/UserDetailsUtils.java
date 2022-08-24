package com.cts.authorization.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDetailsUtils {


//    public static void main(String[] args) {
//        registerStudent();
//    }

    /**
     * Any user can access this API - No Authentication required
     * @return
     */

    public static String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    public static String extractUsername(String email){
        return email.split("@")[0];
    }
}
