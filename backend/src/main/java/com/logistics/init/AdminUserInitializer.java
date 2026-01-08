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
 * 初始化管理员用户
 */
@Component
public class AdminUserInitializer implements ApplicationRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 检查是否已存在admin用户
        User adminUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", "admin"));
        
        if (adminUser == null) {
            // 创建管理员用户
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("root")); // 使用加密密码
            user.setRole("admin");
            user.setStatus("active");
            
            // 使用唯一的邮箱和手机号，避免与现有数据冲突
            user.setEmail("admin_" + System.currentTimeMillis() + "@example.com");
            user.setPhone("1380013" + String.format("%04d", (int)(Math.random() * 10000)));
            
            try {
                userMapper.insert(user);
                System.out.println("已成功创建管理员用户: admin/root");
            } catch (Exception e) {
                System.err.println("创建管理员用户失败: " + e.getMessage());
                // 如果创建失败，尝试只设置必填字段
                User minimalUser = new User();
                minimalUser.setUsername("admin");
                minimalUser.setPassword(passwordEncoder.encode("root"));
                minimalUser.setRole("admin");
                minimalUser.setStatus("active");
                
                try {
                    userMapper.insert(minimalUser);
                    System.out.println("已使用最小字段集成功创建管理员用户: admin/root");
                } catch (Exception ex) {
                    System.err.println("使用最小字段集创建管理员用户也失败了: " + ex.getMessage());
                }
            }
        } else {
            System.out.println("管理员用户已存在");
        }
    }
}