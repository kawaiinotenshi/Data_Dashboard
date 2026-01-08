package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.User;
import com.logistics.vo.UserVO;
import com.logistics.vo.UserRequestVO;

import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {
    List<User> getAllUsers();
    User getUserById(Long id);
    List<User> searchUsers(String keyword);
    boolean createUser(User user);
    boolean updateUser(Long id, User user);
    boolean deleteUser(Long id);
    boolean batchDeleteUsers(List<Long> ids);
    
    // VO相关方法
    List<UserVO> getAllUserVOs();
    UserVO getUserVO(Long id);
    UserVO convertToVO(User user);
    
    // RequestVO相关方法
    boolean createUserByRequestVO(UserRequestVO requestVO);
    boolean updateUserByRequestVO(Long id, UserRequestVO requestVO);
}