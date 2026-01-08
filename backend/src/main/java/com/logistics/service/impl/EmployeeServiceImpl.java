package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Employee;
import com.logistics.mapper.EmployeeMapper;
import com.logistics.service.EmployeeService;
import com.logistics.vo.EmployeeVO;
import com.logistics.vo.EmployeeRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 员工Service实现类
 */
@Service
public class EmployeeServiceImpl extends BaseEntityServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    protected String getEntityName() {
        return "员工";
    }

    @Override
    protected Class<?> getVOClass() {
        return EmployeeVO.class;
    }

    @Override
    protected Class<?> getRequestVOClass() {
        return EmployeeRequestVO.class;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return getAllEntities();
    }

    @Override
    public Employee getEmployeeWithDepartment(Long id) {
        return employeeMapper.getEmployeeWithDepartment(id);
    }

    @Override
    public List<Employee> getAllEmployeesWithDepartment() {
        return employeeMapper.getAllEmployeesWithDepartment();
    }

    @Override
    public List<Employee> searchEmployees(String name, Long departmentId, Double minSalary, Double maxSalary) {
        return employeeMapper.searchEmployees(name, departmentId, minSalary, maxSalary);
    }

    @Override
    public List<Map<String, Object>> getDepartmentStatistics() {
        return employeeMapper.getDepartmentStatistics();
    }

    @Override
    public Employee getHighestPaidEmployee() {
        return employeeMapper.getHighestPaidEmployee();
    }

    @Override
    public boolean createEmployee(Employee employee) {
        return save(employee);
    }

    @Override
    public boolean updateEmployee(Long id, Employee employee) {
        return updateById(employee);
    }

    @Override
    public boolean deleteEmployee(Long id) {
        return deleteEntity(id);
    }

    @Override
    public boolean batchDeleteEmployees(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    // 额外的方法：将Employee转换为EmployeeVO
    public EmployeeVO convertToVO(Employee employee) {
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        if (employee.getDepartment() != null) {
            vo.setDepartmentName(employee.getDepartment().getName());
        }
        return vo;
    }

    // 额外的方法：获取所有员工的VO列表
    public List<EmployeeVO> getAllEmployeeVOs() {
        Function<Employee, EmployeeVO> voMapper = this::convertToVO;
        return getAllEntityVOs(voMapper);
    }

    // 额外的方法：获取所有带部门信息的员工VO列表
    public List<EmployeeVO> getAllEmployeesWithDepartmentVOs() {
        List<Employee> employees = getAllEmployeesWithDepartment();
        return convertToVOList(employees, this::convertToVO);
    }

    // 额外的方法：根据ID获取带部门信息的员工VO
    public EmployeeVO getEmployeeWithDepartmentVO(Long id) {
        Employee employee = getEmployeeWithDepartment(id);
        return convertToVO(employee);
    }

    // 额外的方法：通过RequestVO创建员工
    public boolean createEmployeeByRequestVO(EmployeeRequestVO requestVO) {
        return createEntity(requestVO, Employee.class);
    }

    // 额外的方法：通过RequestVO更新员工
    public boolean updateEmployeeByRequestVO(Long id, EmployeeRequestVO requestVO) {
        return updateEntity(id, requestVO);
    }
}