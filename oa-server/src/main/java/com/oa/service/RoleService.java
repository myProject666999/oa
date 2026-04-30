package com.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> getRolesByUserId(Long userId);

    List<Long> getMenuIdsByRoleId(Long roleId);

    boolean saveRoleMenus(Long roleId, List<Long> menuIds);
}