package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.EmployeeService;
import com.logistics.service.DepartmentService;
import com.logistics.vo.EmployeeVO;
import com.logistics.vo.EmployeeRequestVO;
import com.logistics.vo.DepartmentVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public Result<List<EmployeeVO>> getAllEmployees() {
        List<EmployeeVO> list = employeeService.getAllEmployeeVOs();
        return Result.success(list);
    }

    @GetMapping("/list-with-department")
    public Result<List<EmployeeVO>> getAllEmployeesWithDepartment() {
        List<EmployeeVO> list = employeeService.getAllEmployeesWithDepartmentVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<EmployeeVO> getEmployeeById(@PathVariable Long id) {
        EmployeeVO employeeVO = employeeService.getEmployeeWithDepartmentVO(id);
        return Result.success(employeeVO);
    }

    @GetMapping("/search")
    public Result<List<EmployeeVO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {
        List<EmployeeVO> list = employeeService.searchEmployees(name, departmentId, minSalary, maxSalary)
                .stream()
                .map(employeeService::convertToVO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    @GetMapping("/department-statistics")
    public Result<List<Map<String, Object>>> getDepartmentStatistics() {
        List<Map<String, Object>> statistics = employeeService.getDepartmentStatistics();
        return Result.success(statistics);
    }

    @GetMapping("/highest-paid")
    public Result<EmployeeVO> getHighestPaidEmployee() {
        EmployeeVO employeeVO = employeeService.convertToVO(employeeService.getHighestPaidEmployee());
        return Result.success(employeeVO);
    }

    @PostMapping
    public Result<String> createEmployee(@RequestBody EmployeeRequestVO employeeRequestVO) {
        boolean success = employeeService.createEmployeeByRequestVO(employeeRequestVO);
        if (success) {
            return Result.success("创建员工成功");
        } else {
            return Result.error("创建员工失败");
        }
    }

    @PutMapping("/{id}")
    public Result<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestVO employeeRequestVO) {
        boolean success = employeeService.updateEmployeeByRequestVO(id, employeeRequestVO);
        if (success) {
            return Result.success("更新员工成功");
        } else {
            return Result.error("更新员工失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteEmployee(@PathVariable Long id) {
        boolean success = employeeService.deleteEmployee(id);
        if (success) {
            return Result.success("删除员工成功");
        } else {
            return Result.error("删除员工失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<String> batchDeleteEmployees(@RequestBody List<Long> ids) {
        boolean success = employeeService.batchDeleteEmployees(ids);
        if (success) {
            return Result.success("批量删除员工成功");
        } else {
            return Result.error("批量删除员工失败");
        }
    }


}