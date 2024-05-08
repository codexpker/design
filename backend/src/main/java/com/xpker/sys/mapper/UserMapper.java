package com.xpker.sys.mapper;

import com.xpker.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
public interface UserMapper extends BaseMapper<User> {
    public List<String> selectRoleNameById(Integer userId);
}
