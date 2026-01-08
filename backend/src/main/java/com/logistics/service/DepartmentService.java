package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Department;
import com.logistics.vo.DepartmentVO;

import java.util.List;
import java.util.Map;

/**
 * 部门Service接口
 */
public interface DepartmentService extends IService<Department> {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    boolean createDepartment(Department department);
    boolean updateDepartment(Long id, Department department);
    boolean deleteDepartment(Long id);
    boolean batchDeleteDepartments(List<Long> ids);
    
    // VO相关方法
    List<DepartmentVO> getAllDepartmentVOs();
    DepartmentVO getDepartmentVOById(Long id);
}

