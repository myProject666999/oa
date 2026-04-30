package com.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.deleted = '0' " +
            "ORDER BY m.order_num ASC")
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
}
