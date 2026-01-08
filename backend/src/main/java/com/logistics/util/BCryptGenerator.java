package com.logistics.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("请提供要加密的密码作为参数");
            return;
        }
        
        String password = args[0];
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("加密后的密码: " + encodedPassword);
    }
}