package com.logistics.controller;

import com.logistics.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AiController {

    @Autowired
    private RestTemplate restTemplate;

    // 硅基流动API配置
    private static final String API_KEY = "sk-zzgfrcfiyazabzncavbzkxvgzkqcypheaiuapxxqaoihqbhq";
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String MODEL_NAME = "deepseek-ai/DeepSeek-R1-0528-Qwen3-8B";

    @PostMapping("/chat")
    public Result<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        String message = (String) request.get("message");

        if (message == null || message.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("reply", "请输入您的问题。");
            errorResponse.put("message", "请输入您的问题。");
            return Result.success(errorResponse);
        }

        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL_NAME);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);

            // 构建消息历史
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);
            requestBody.put("messages", messages);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            // 创建请求实体
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            // 处理响应
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = responseEntity.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> messageMap = (Map<String, String>) choice.get("message");
                    String reply = messageMap.get("content");

                    Map<String, Object> response = new HashMap<>();
                    response.put("reply", reply);
                    response.put("message", reply);
                    return Result.success(response);
                }
            }

            // 如果响应异常，返回默认错误回复
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("reply", "抱歉，AI服务暂时不可用，请稍后重试。");
            errorResponse.put("message", "抱歉，AI服务暂时不可用，请稍后重试。");
            return Result.success(errorResponse);

        } catch (Exception e) {
            // 处理异常情况
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("reply", "抱歉，处理您的请求时出现错误，请稍后重试。");
            errorResponse.put("message", "抱歉，处理您的请求时出现错误，请稍后重试。");
            return Result.success(errorResponse);
        }
    }
}