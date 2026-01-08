package com.logistics.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.User;
import com.logistics.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 更新用户密码为BCrypt加密格式
 */
@Component
public class UserPasswordUpdater implements ApplicationRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 检查并更新用户'111111'的密码
        User user111111 = userMapper.selectOne(new QueryWrapper<User>().eq("username", "111111"));
        
        if (user111111 != null) {
            // 检查密码是否已经是加密格式（BCrypt密码以$2a$或$2b$开头）
            String currentPassword = user111111.getPassword();
            if (currentPassword != null && !currentPassword.startsWith("$2a$") && !currentPassword.startsWith("$2b$")) {
                // 更新为加密密码
                user111111.setPassword(passwordEncoder.encode("111111"));
                userMapper.updateById(user111111);
                System.out.println("已更新用户'111111'的密码为BCrypt加密格式");
            } else {
                System.out.println("用户'111111'的密码已经是加密格式");
            }
        } else {
            System.out.println("未找到用户'111111'");
        }
    }
}