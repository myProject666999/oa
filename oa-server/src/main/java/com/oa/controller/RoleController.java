package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.entity.Menu;
import com.oa.entity.Role;
import com.oa.service.MenuService;
import com.oa.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<Role>> list(Role role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (role != null) {
            if (StringUtils.hasText(role.getRoleName())) {
                wrapper.like(Role::getRoleName, role.getRoleName());
            }
            if (StringUtils.hasText(role.getStatus())) {
                wrapper.eq(Role::getStatus, role.getStatus());
            }
        }
        wrapper.eq(Role::getDeleted, "0");
        wrapper.orderByAsc(Role::getId);
        List<Role> list = roleService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Boolean> add(@RequestBody Role role) {
        role.setDeleted("0");
        boolean result = roleService.save(role);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Boolean> update(@RequestBody Role role) {
        boolean result = roleService.updateById(role);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Role role = new Role();
        role.setId(id);
        role.setDeleted("1");
        boolean result = roleService.updateById(role);
        return Result.success(result);
    }

    @GetMapping("/menuIds/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<List<Long>> getMenuIds(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }

    @PostMapping("/saveMenus")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Boolean> saveMenus(@RequestParam Long roleId, @RequestBody List<Long> menuIds) {
        boolean result = roleService.saveRoleMenus(roleId, menuIds);
        return Result.success(result);
    }
}