package com.logistics.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.logistics.entity.Department;
import com.logistics.mapper.DepartmentMapper;
import com.logistics.service.DepartmentService;
import com.logistics.vo.DepartmentVO;
import com.logistics.vo.DepartmentRequestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

/**
 * 部门Service实现类
 */
@Service
public class DepartmentServiceImpl extends BaseEntityServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    protected String getEntityName() {
        return "部门";
    }

    @Override
    protected Class<?> getVOClass() {
        return DepartmentVO.class;
    }

    @Override
    protected Class<?> getRequestVOClass() {
        return DepartmentRequestVO.class;
    }

    @Override
    public List<Department> getAllDepartments() {
        return getAllEntities();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return getById(id);
    }

    @Override
    public boolean createDepartment(Department department) {
        return save(department);
    }

    @Override
    public boolean updateDepartment(Long id, Department department) {
        // 检查部门是否存在
        if (getById(id) == null) {
            return false;
        }
        return updateById(department);
    }

    @Override
    public boolean deleteDepartment(Long id) {
        return deleteEntity(id);
    }

    @Override
    public boolean batchDeleteDepartments(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    // 额外的方法：将Department转换为DepartmentVO
    public DepartmentVO convertToVO(Department department) {
        DepartmentVO vo = new DepartmentVO();
        BeanUtils.copyProperties(department, vo);
        return vo;
    }

    // 额外的方法：获取所有部门的VO列表
    public List<DepartmentVO> getAllDepartmentVOs() {
        Function<Department, DepartmentVO> voMapper = this::convertToVO;
        return getAllEntityVOs(voMapper);
    }

    // 额外的方法：根据ID获取部门VO
    public DepartmentVO getDepartmentVOById(Long id) {
        Function<Department, DepartmentVO> voMapper = this::convertToVO;
        return getEntityById(id, voMapper);
    }

    // 额外的方法：通过RequestVO创建部门
    public boolean createDepartmentByRequestVO(DepartmentRequestVO requestVO) {
        return createEntity(requestVO, Department.class);
    }

    // 额外的方法：通过RequestVO更新部门
    public boolean updateDepartmentByRequestVO(Long id, DepartmentRequestVO requestVO) {
        return updateEntity(id, requestVO);
    }
}