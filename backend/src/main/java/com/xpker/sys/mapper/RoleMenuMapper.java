package com.xpker.sys.mapper;

import com.xpker.sys.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<Integer> getMenuIdByRoleId(Integer roleId);
}
