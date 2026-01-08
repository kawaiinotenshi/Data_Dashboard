package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.UserService;
import com.logistics.vo.UserVO;
import com.logistics.vo.UserRequestVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public Result<List<UserVO>> getAllUsers() {
        List<UserVO> list = userService.getAllUserVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.getUserVO(id);
        return Result.success(userVO);
    }

    @PostMapping
    public Result<String> createUser(@RequestBody UserRequestVO userRequestVO) {
        boolean success = userService.createUserByRequestVO(userRequestVO);
        if (success) {
            return Result.success("创建用户成功");
        } else {
            return Result.error("创建用户失败");
        }
    }

    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody UserRequestVO userRequestVO) {
        boolean success = userService.updateUserByRequestVO(id, userRequestVO);
        if (success) {
            return Result.success("更新用户成功");
        } else {
            return Result.error("更新用户失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success("删除用户成功");
        } else {
            return Result.error("删除用户失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<String> batchDeleteUsers(@RequestBody List<Long> ids) {
        boolean success = userService.batchDeleteUsers(ids);
        if (success) {
            return Result.success("批量删除用户成功");
        } else {
            return Result.error("批量删除用户失败");
        }
    }

    @GetMapping("/search")
    public Result<List<UserVO>> searchUsers(@RequestParam(required = false) String keyword) {
        List<UserVO> list = userService.searchUsers(keyword)
                .stream()
                .map(userService::convertToVO)
                .collect(java.util.stream.Collectors.toList());
        return Result.success(list);
    }
}