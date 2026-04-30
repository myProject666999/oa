package com.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.entity.Menu;
import com.oa.mapper.MenuMapper;
import com.oa.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return baseMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }

        Map<Long, List<Menu>> menuMap = menus.stream()
                .collect(Collectors.groupingBy(Menu::getParentId));

        List<Menu> topMenus = menuMap.getOrDefault(0L, new ArrayList<>());
        buildChildren(topMenus, menuMap);
        return topMenus;
    }

    private void buildChildren(List<Menu> menus, Map<Long, List<Menu>> menuMap) {
        for (Menu menu : menus) {
            List<Menu> children = menuMap.get(menu.getId());
            if (!CollectionUtils.isEmpty(children)) {
                menu.setChildren(children);
                buildChildren(children, menuMap);
            }
        }
    }

    @Override
    public List<Long> getChildIds(List<Long> menuIds) {
        List<Long> allIds = new ArrayList<>(menuIds);
        List<Long> currentIds = new ArrayList<>(menuIds);

        while (!currentIds.isEmpty()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getParentId, currentIds);
            wrapper.eq(Menu::getDeleted, "0");
            List<Menu> children = this.list(wrapper);
            
            List<Long> childIds = children.stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());
            
            allIds.addAll(childIds);
            currentIds = childIds;
        }
        return allIds;
    }
}