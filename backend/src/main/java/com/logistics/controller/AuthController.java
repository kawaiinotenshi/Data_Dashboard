package com.logistics.controller;

import com.logistics.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtUtil.generateToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "登录成功");
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", username);
            data.put("expiresIn", 86400000L);
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 401);
            errorResponse.put("message", "用户名或密码错误");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String newToken = jwtUtil.refreshToken(token);

            if (newToken != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("message", "Token刷新成功");
                
                Map<String, Object> data = new HashMap<>();
                data.put("token", newToken);
                data.put("expiresIn", 86400000L);
                response.put("data", data);
                
                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 401);
        errorResponse.put("message", "Token无效或已过期");
        return ResponseEntity.status(401).body(errorResponse);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);

            if (username != null && !jwtUtil.isTokenExpired(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 200);
                response.put("message", "Token有效");
                
                Map<String, Object> data = new HashMap<>();
                data.put("username", username);
                data.put("valid", true);
                response.put("data", data);
                
                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 401);
        errorResponse.put("message", "Token无效或已过期");
        return ResponseEntity.status(401).body(errorResponse);
    }
}
