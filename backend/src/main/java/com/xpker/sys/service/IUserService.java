package com.xpker.sys.service;

import com.xpker.common.vo.Result;
import com.xpker.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xpker
 * @since 2023-07-06
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getInfo(String token);

    void logout(String token);

    void addUser(User user);

    User getUserById(Integer id);

    void updateByUser(User user);

    void deleteById(Integer id);
}
