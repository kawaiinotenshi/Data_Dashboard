package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.DepartmentService;
import com.logistics.vo.DepartmentVO;
import com.logistics.vo.DepartmentRequestVO;
import com.logistics.entity.Department;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@CrossOrigin
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public Result<List<DepartmentVO>> getAllDepartments() {
        List<DepartmentVO> list = departmentService.getAllDepartmentVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<DepartmentVO> getDepartmentById(@PathVariable Long id) {
        DepartmentVO departmentVO = departmentService.getDepartmentVOById(id);
        return Result.success(departmentVO);
    }

    @PostMapping
    public Result<String> createDepartment(@RequestBody DepartmentRequestVO departmentRequestVO) {
        Department department = new Department();
        department.setName(departmentRequestVO.getName());
        department.setDescription(departmentRequestVO.getDescription());
        
        boolean success = departmentService.createDepartment(department);
        if (success) {
            return Result.success("创建部门成功");
        } else {
            return Result.error("创建部门失败");
        }
    }

    @PutMapping("/{id}")
    public Result<String> updateDepartment(@PathVariable Long id, @RequestBody DepartmentRequestVO departmentRequestVO) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        
        department.setName(departmentRequestVO.getName());
        department.setDescription(departmentRequestVO.getDescription());
        
        boolean success = departmentService.updateDepartment(id, department);
        if (success) {
            return Result.success("更新部门成功");
        } else {
            return Result.error("更新部门失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteDepartment(@PathVariable Long id) {
        boolean success = departmentService.deleteDepartment(id);
        if (success) {
            return Result.success("删除部门成功");
        } else {
            return Result.error("删除部门失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<String> batchDeleteDepartments(@RequestBody List<Long> ids) {
        boolean success = departmentService.batchDeleteDepartments(ids);
        if (success) {
            return Result.success("批量删除部门成功");
        } else {
            return Result.error("批量删除部门失败");
        }
    }
}