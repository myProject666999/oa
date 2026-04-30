package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.entity.Menu;
import com.oa.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<Menu>> tree(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (menu != null) {
            if (StringUtils.hasText(menu.getMenuName())) {
                wrapper.like(Menu::getMenuName, menu.getMenuName());
            }
            if (StringUtils.hasText(menu.getStatus())) {
                wrapper.eq(Menu::getStatus, menu.getStatus());
            }
        }
        wrapper.eq(Menu::getDeleted, "0");
        wrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> list = menuService.list(wrapper);
        List<Menu> tree = menuService.buildMenuTree(list);
        return Result.success(tree);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<Menu>> list(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (menu != null) {
            if (StringUtils.hasText(menu.getMenuName())) {
                wrapper.like(Menu::getMenuName, menu.getMenuName());
            }
            if (StringUtils.hasText(menu.getStatus())) {
                wrapper.eq(Menu::getStatus, menu.getStatus());
            }
        }
        wrapper.eq(Menu::getDeleted, "0");
        wrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> list = menuService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Boolean> add(@RequestBody Menu menu) {
        menu.setDeleted("0");
        boolean result = menuService.save(menu);
        return Result.success(result);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Boolean> update(@RequestBody Menu menu) {
        boolean result = menuService.updateById(menu);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public Result<Boolean> delete(@PathVariable Long id) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setDeleted("1");
        boolean result = menuService.updateById(menu);
        return Result.success(result);
    }
}