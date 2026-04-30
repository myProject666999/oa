package com.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Department;
import com.oa.mapper.DepartmentMapper;
import com.oa.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> buildDeptTree(List<Department> depts) {
        if (CollectionUtils.isEmpty(depts)) {
            return new ArrayList<>();
        }

        Map<Long, List<Department>> deptMap = depts.stream()
                .collect(Collectors.groupingBy(Department::getParentId));

        List<Department> topDepts = deptMap.getOrDefault(0L, new ArrayList<>());
        buildChildren(topDepts, deptMap);
        return topDepts;
    }

    private void buildChildren(List<Department> depts, Map<Long, List<Department>> deptMap) {
        for (Department dept : depts) {
            List<Department> children = deptMap.get(dept.getId());
            if (!CollectionUtils.isEmpty(children)) {
                dept.setChildren(children);
                buildChildren(children, deptMap);
            }
        }
    }
}
