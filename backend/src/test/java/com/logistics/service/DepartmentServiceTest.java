package com.logistics.service;

import com.logistics.entity.Department;
import com.logistics.mapper.DepartmentMapper;
import com.logistics.service.impl.DepartmentServiceImpl;
import com.logistics.vo.DepartmentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentMapper departmentMapper;

    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        // 手动创建 departmentService 对象
        departmentService = new DepartmentServiceImpl();
        
        // 设置 ServiceImpl 中的 baseMapper
        ReflectionTestUtils.setField(departmentService, "baseMapper", departmentMapper);
        
        // 设置 DepartmentServiceImpl 中的 departmentMapper
        ReflectionTestUtils.setField(departmentService, "departmentMapper", departmentMapper);
    }

    @Test
    public void testGetAllDepartments() {
        // 准备测试数据
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("技术部");
        department1.setDescription("负责技术开发");

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("市场部");
        department2.setDescription("负责市场推广");

        List<Department> departments = Arrays.asList(department1, department2);

        // 模拟Mapper行为
        when(departmentMapper.selectList(any())).thenReturn(departments);

        // 调用服务方法
        List<Department> result = departmentService.getAllDepartments();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("技术部", result.get(0).getName());
        assertEquals("市场部", result.get(1).getName());

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).selectList(any());
    }

    @Test
    public void testGetDepartmentById() {
        // 准备测试数据
        Department department = new Department();
        department.setId(1L);
        department.setName("技术部");
        department.setDescription("负责技术开发");

        // 模拟Mapper行为
        when(departmentMapper.selectById(1L)).thenReturn(department);

        // 调用服务方法
        Department result = departmentService.getDepartmentById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("技术部", result.getName());
        assertEquals("负责技术开发", result.getDescription());

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).selectById(1L);
    }

    @Test
    public void testCreateDepartment() {
        // 准备测试数据
        Department department = new Department();
        department.setName("财务部");
        department.setDescription("负责财务核算");

        // 模拟Mapper行为
        when(departmentMapper.insert(any(Department.class))).thenReturn(1);

        // 调用服务方法
        boolean result = departmentService.createDepartment(department);

        // 验证结果
        assertTrue(result);

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).insert(any(Department.class));
    }

    @Test
    public void testUpdateDepartment() {
        // 准备测试数据
        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("技术部");
        existingDepartment.setDescription("负责技术开发");

        Department updatedDepartment = new Department();
        updatedDepartment.setName("技术研发部");
        updatedDepartment.setDescription("负责技术研发工作");

        // 模拟Mapper行为
        when(departmentMapper.selectById(1L)).thenReturn(existingDepartment);
        when(departmentMapper.updateById(any(Department.class))).thenReturn(1);

        // 调用服务方法
        boolean result = departmentService.updateDepartment(1L, updatedDepartment);

        // 验证结果
        assertTrue(result);

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).selectById(1L);
        verify(departmentMapper, times(1)).updateById(any(Department.class));
    }

    @Test
    public void testDeleteDepartment() {
        // 直接模拟ServiceImpl的removeById方法
        when(departmentService.removeById(1L)).thenReturn(true);

        // 调用服务方法
        boolean result = departmentService.deleteDepartment(1L);

        // 验证结果
        assertTrue(result);

        // 验证removeById方法被调用
        verify(departmentService, times(1)).removeById(1L);
    }

    @Test
    public void testBatchDeleteDepartments() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // 直接模拟ServiceImpl的removeByIds方法
        when(departmentService.removeByIds(ids)).thenReturn(true);

        // 调用服务方法
        boolean result = departmentService.batchDeleteDepartments(ids);

        // 验证结果
        assertTrue(result);

        // 验证removeByIds方法被调用
        verify(departmentService, times(1)).removeByIds(ids);
    }

    @Test
    public void testGetAllDepartmentVOs() {
        // 准备测试数据
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("技术部");
        department1.setDescription("负责技术开发");

        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("市场部");
        department2.setDescription("负责市场推广");

        List<Department> departments = Arrays.asList(department1, department2);

        // 模拟Mapper行为
        when(departmentMapper.selectList(any())).thenReturn(departments);

        // 调用服务方法
        List<DepartmentVO> result = departmentService.getAllDepartmentVOs();

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("技术部", result.get(0).getName());
        assertEquals("市场部", result.get(1).getName());

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).selectList(any());
    }

    @Test
    public void testGetDepartmentVOById() {
        // 准备测试数据
        Department department = new Department();
        department.setId(1L);
        department.setName("技术部");
        department.setDescription("负责技术开发");

        // 模拟Mapper行为
        when(departmentMapper.selectById(1L)).thenReturn(department);

        // 调用服务方法
        DepartmentVO result = departmentService.getDepartmentVOById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("技术部", result.getName());
        assertEquals("负责技术开发", result.getDescription());

        // 验证Mapper方法被调用
        verify(departmentMapper, times(1)).selectById(1L);
    }
}