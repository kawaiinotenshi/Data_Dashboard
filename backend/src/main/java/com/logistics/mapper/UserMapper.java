package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.User;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据条件搜索用户
     */
    List<User> searchUsers(@Param("keyword") String keyword);
}