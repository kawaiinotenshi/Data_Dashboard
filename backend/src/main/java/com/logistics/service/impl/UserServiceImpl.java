package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.logistics.entity.User;
import com.logistics.mapper.UserMapper;
import com.logistics.service.UserService;
import com.logistics.vo.UserRequestVO;
import com.logistics.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends BaseEntityServiceImpl<UserMapper, User> implements UserService {

    @Override
    protected String getEntityName() {
        return "用户";
    }

    @Override
    protected Class<?> getVOClass() {
        return UserVO.class;
    }

    @Override
    protected Class<?> getRequestVOClass() {
        return UserRequestVO.class;
    }

    @Override
    public List<User> getAllUsers() {
        return getAllEntities();
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("username", keyword)
                    .or().like("email", keyword)
                    .or().like("phone", keyword);
        }
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(Long id, User user) {
        user.setId(id);
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUsers(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public List<UserVO> getAllUserVOs() {
        return getAllEntityVOs(this::convertToVO);
    }

    @Override
    public UserVO getUserVO(Long id) {
        return getEntityById(id, this::convertToVO);
    }

    @Override
    public UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUserByRequestVO(UserRequestVO requestVO) {
        User user = new User();
        BeanUtils.copyProperties(requestVO, user);
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserByRequestVO(Long id, UserRequestVO requestVO) {
        User user = getById(id);
        if (user == null) {
            return false;
        }
        BeanUtils.copyProperties(requestVO, user);
        user.setId(id);
        return updateById(user);
    }
}