package com.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    List<Department> buildDeptTree(List<Department> depts);
}
