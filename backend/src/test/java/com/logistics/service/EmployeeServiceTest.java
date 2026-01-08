package com.logistics.service;

import com.logistics.entity.Employee;
import com.logistics.entity.Department;
import com.logistics.mapper.EmployeeMapper;
import com.logistics.mapper.DepartmentMapper;
import com.logistics.service.impl.EmployeeServiceImpl;
import com.logistics.service.impl.BaseEntityServiceImpl;
import com.logistics.vo.EmployeeVO;
import com.logistics.vo.EmployeeRequestVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentMapper departmentMapper;

    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        // 创建EmployeeService实例并注入模拟的mapper
        employeeService = new EmployeeServiceImpl();
        EmployeeServiceImpl employeeServiceImpl = (EmployeeServiceImpl) employeeService;
        
        // 使用反射设置私有字段
        try {
            java.lang.reflect.Field employeeMapperField = EmployeeServiceImpl.class.getDeclaredField("employeeMapper");
            employeeMapperField.setAccessible(true);
            employeeMapperField.set(employeeServiceImpl, employeeMapper);
            
            // 设置父类的baseMapper字段
            java.lang.reflect.Field baseMapperField = BaseEntityServiceImpl.class.getDeclaredField("baseMapper");
            baseMapperField.setAccessible(true);
            baseMapperField.set(employeeServiceImpl, employeeMapper);
        } catch (Exception e) {
            throw new RuntimeException("设置mapper失败", e);
        }
    }

    @Test
    public void testGetEmployeeById() {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("张三");
        employee.setDepartmentId(1L);
        employee.setSalary(5000.0);

        // 模拟Mapper行为
        when(employeeMapper.selectById(1L)).thenReturn(employee);

        // 调用服务方法（使用MyBatis-Plus通用方法）
        Employee result = employeeService.getById(1L);

        // 验证结果
        assertEquals("张三", result.getName());
        assertEquals(1L, result.getDepartmentId());
        assertEquals(5000.0, result.getSalary());

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).selectById(1L);
    }

    @Test
    public void testGetAllEmployees() {
        // 准备测试数据
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("张三");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("李四");

        List<Employee> employees = Arrays.asList(employee1, employee2);

        // 模拟Mapper行为
        when(employeeMapper.selectList(any())).thenReturn(employees);

        // 调用服务方法
        List<Employee> result = employeeService.getAllEmployees();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("张三", result.get(0).getName());
        assertEquals("李四", result.get(1).getName());

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).selectList(any());
    }

    @Test
    public void testAddEmployee() {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setName("王五");
        employee.setDepartmentId(1L);
        employee.setSalary(6000.0);

        // 模拟Mapper行为
        when(employeeMapper.insert(any(Employee.class))).thenReturn(1);

        // 调用服务方法
        boolean result = employeeService.createEmployee(employee);

        // 验证结果
        assertEquals(true, result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).insert(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("张三");
        employee.setDepartmentId(1L);
        employee.setSalary(5500.0);

        // 模拟Mapper行为
        when(employeeMapper.updateById(any(Employee.class))).thenReturn(1);

        // 调用服务方法
        boolean result = employeeService.updateEmployee(1L, employee);

        // 验证结果
        assertEquals(true, result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).updateById(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        // 模拟Mapper行为 - 使用doReturn().when()语法
        doReturn(1).when(employeeMapper).deleteById(1L);

        // 调用服务方法
        boolean result = employeeService.deleteEmployee(1L);

        // 验证结果
        assertEquals(true, result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).deleteById(1L);
    }

    @Test
    public void testBatchDeleteEmployees() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // 模拟Mapper行为 - 使用doReturn().when()语法
        doReturn(1).when(employeeMapper).deleteById(anyLong());

        // 调用服务方法
        boolean result = employeeService.batchDeleteEmployees(ids);

        // 验证结果
        assertTrue(result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(ids.size())).deleteById(anyLong());
    }

    @Test
    public void testSearchEmployees() {
        // 准备测试数据
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("张三");
        employee1.setDepartmentId(1L);
        employee1.setSalary(5000.0);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("李四");
        employee2.setDepartmentId(1L);
        employee2.setSalary(6000.0);

        List<Employee> employees = Arrays.asList(employee1, employee2);

        // 模拟Mapper行为
        when(employeeMapper.searchEmployees("张", 1L, 4000.0, 7000.0)).thenReturn(employees);

        // 调用服务方法
        List<Employee> result = employeeService.searchEmployees("张", 1L, 4000.0, 7000.0);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("张三", result.get(0).getName());
        assertEquals("李四", result.get(1).getName());

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).searchEmployees("张", 1L, 4000.0, 7000.0);
    }

    @Test
    public void testGetHighestPaidEmployee() {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setId(2L);
        employee.setName("李四");
        employee.setSalary(8000.0);

        // 模拟Mapper行为
        when(employeeMapper.getHighestPaidEmployee()).thenReturn(employee);

        // 调用服务方法
        Employee result = employeeService.getHighestPaidEmployee();

        // 验证结果
        assertNotNull(result);
        assertEquals("李四", result.getName());
        assertEquals(8000.0, result.getSalary());

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).getHighestPaidEmployee();
    }

    @Test
    public void testGetDepartmentStatistics() {
        // 准备测试数据
        Map<String, Object> stat1 = new HashMap<>();
        stat1.put("departmentName", "技术部");
        stat1.put("employeeCount", 10);
        stat1.put("averageSalary", 8000.0);

        Map<String, Object> stat2 = new HashMap<>();
        stat2.put("departmentName", "市场部");
        stat2.put("employeeCount", 5);
        stat2.put("averageSalary", 6000.0);

        List<Map<String, Object>> statistics = Arrays.asList(stat1, stat2);

        // 模拟Mapper行为
        when(employeeMapper.getDepartmentStatistics()).thenReturn(statistics);

        // 调用服务方法
        List<Map<String, Object>> result = employeeService.getDepartmentStatistics();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("技术部", result.get(0).get("departmentName"));
        assertEquals(10, result.get(0).get("employeeCount"));
        assertEquals(8000.0, result.get(0).get("averageSalary"));

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).getDepartmentStatistics();
    }

    @Test
    public void testConvertToVO() {
        // 准备测试数据
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("张三");
        employee.setDepartmentId(1L);
        employee.setSalary(5000.0);

        // 调用服务方法
        EmployeeVO result = employeeService.convertToVO(employee);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("张三", result.getName());
        assertEquals(1L, result.getDepartmentId());
        assertEquals(5000.0, result.getSalary());
    }

    @Test
    public void testCreateEmployeeByRequestVO() {
        // 准备测试数据
        EmployeeRequestVO requestVO = new EmployeeRequestVO();
        requestVO.setName("王五");
        requestVO.setDepartmentId(1L);
        requestVO.setSalary(6000.0);

        // 模拟Mapper行为
        when(employeeMapper.insert(any(Employee.class))).thenReturn(1);

        // 调用服务方法
        boolean result = employeeService.createEmployeeByRequestVO(requestVO);

        // 验证结果
        assertTrue(result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).insert(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeByRequestVO() {
        // 准备测试数据
        EmployeeRequestVO requestVO = new EmployeeRequestVO();
        requestVO.setName("张三更新");
        requestVO.setDepartmentId(2L);
        requestVO.setSalary(6000.0);

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setName("张三");
        existingEmployee.setDepartmentId(1L);
        existingEmployee.setSalary(5000.0);

        // 模拟Mapper行为
        when(employeeMapper.selectById(1L)).thenReturn(existingEmployee);
        when(employeeMapper.updateById(any(Employee.class))).thenReturn(1);

        // 调用服务方法
        boolean result = employeeService.updateEmployeeByRequestVO(1L, requestVO);

        // 验证结果
        assertTrue(result);

        // 验证Mapper方法被调用
        verify(employeeMapper, times(1)).selectById(1L);
        verify(employeeMapper, times(1)).updateById(any(Employee.class));
    }
}
