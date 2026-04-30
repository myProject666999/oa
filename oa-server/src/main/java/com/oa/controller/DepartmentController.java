package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.entity.Department;
import com.oa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dept")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public Result<List<Department>> tree(Department dept) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (dept != null) {
            if (StringUtils.hasText(dept.getDeptName())) {
                wrapper.like(Department::getDeptName, dept.getDeptName());
            }
            if (StringUtils.hasText(dept.getStatus())) {
                wrapper.eq(Department::getStatus, dept.getStatus());
            }
        }
        wrapper.eq(Department::getDeleted, "0");
        wrapper.orderByAsc(Department::getOrderNum);
        List<Department> list = departmentService.list(wrapper);
        List<Department> tree = buildDeptTree(list);
        return Result.success(tree);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:dept:list')")
    public Result<List<Department>> list(Department dept) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (dept != null) {
            if (StringUtils.hasText(dept.getDeptName())) {
                wrapper.like(Department::getDeptName, dept.getDeptName());
            }
            if (StringUtils.hasText(dept.getStatus())) {
                wrapper.eq(Department::getStatus, dept.getStatus());
            }
        }
        wrapper.eq(Department::getDeleted, "0");
        wrapper.orderByAsc(Department::getOrderNum);
        List<Department> list = departmentService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:dept:query')")
    public Result<Department> getById(@PathVariable Long id) {
        Department dept = departmentService.getById(id);
        return Result.success(dept);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:dept:add')")
    public Result<Boolean> add(@RequestBody Department dept) {
        dept.setDeleted("0");
        boolean result = departmentService.save(dept);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:dept:edit')")
    public Result<Boolean> update(@RequestBody Department dept) {
        boolean result = departmentService.updateById(dept);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:dept:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Department dept = new Department();
        dept.setId(id);
        dept.setDeleted("1");
        boolean result = departmentService.updateById(dept);
        return Result.success(result);
    }

    private List<Department> buildDeptTree(List<Department> depts) {
        Map<Long, List<Department>> deptMap = depts.stream()
                .collect(Collectors.groupingBy(Department::getParentId));
        List<Department> topDepts = deptMap.getOrDefault(0L, depts);
        buildChildren(topDepts, deptMap);
        return topDepts;
    }

    private void buildChildren(List<Department> depts, Map<Long, List<Department>> deptMap) {
        for (Department dept : depts) {
            List<Department> children = deptMap.get(dept.getId());
            if (children != null && !children.isEmpty()) {
                dept.setChildren(children);
                buildChildren(children, deptMap);
            }
        }
    }
}