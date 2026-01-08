package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Employee;
import com.logistics.vo.EmployeeVO;
import com.logistics.vo.EmployeeRequestVO;

import java.util.List;
import java.util.Map;

/**
 * 员工Service接口
 */
public interface EmployeeService extends IService<Employee> {
    List<Employee> getAllEmployees();
    Employee getEmployeeWithDepartment(Long id);
    List<Employee> getAllEmployeesWithDepartment();
    List<Employee> searchEmployees(String name, Long departmentId, Double minSalary, Double maxSalary);
    List<Map<String, Object>> getDepartmentStatistics();
    Employee getHighestPaidEmployee();
    boolean createEmployee(Employee employee);
    boolean updateEmployee(Long id, Employee employee);
    boolean deleteEmployee(Long id);
    boolean batchDeleteEmployees(List<Long> ids);
    
    // VO相关方法
    List<EmployeeVO> getAllEmployeeVOs();
    List<EmployeeVO> getAllEmployeesWithDepartmentVOs();
    EmployeeVO getEmployeeWithDepartmentVO(Long id);
    EmployeeVO convertToVO(Employee employee);
    
    // RequestVO相关方法
    boolean createEmployeeByRequestVO(EmployeeRequestVO requestVO);
    boolean updateEmployeeByRequestVO(Long id, EmployeeRequestVO requestVO);
}
