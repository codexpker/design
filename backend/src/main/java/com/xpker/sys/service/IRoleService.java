package com.xpker.sys.service;

import com.xpker.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer roleId);

    void updateRole(Role role);

    void removeRoleById(Integer roleId);
}
