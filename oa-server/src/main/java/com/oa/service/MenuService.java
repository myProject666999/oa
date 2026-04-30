package com.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> getMenusByUserId(Long userId);

    List<Menu> buildMenuTree(List<Menu> menus);

    List<Long> getChildIds(List<Long> menuIds);
}