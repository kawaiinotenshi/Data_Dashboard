package com.logistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Employee;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 员工Mapper接口
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 根据ID获取员工及其部门信息
     */
    Employee getEmployeeWithDepartment(Long id);

    /**
     * 获取所有员工及其部门信息
     */
    List<Employee> getAllEmployeesWithDepartment();

    /**
     * 根据条件搜索员工
     */
    List<Employee> searchEmployees(@Param("name") String name, @Param("departmentId") Long departmentId, @Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary);

    /**
     * 获取部门统计信息
     */
    List<Map<String, Object>> getDepartmentStatistics();

    /**
     * 获取工资最高的员工
     */
    Employee getHighestPaidEmployee();
}