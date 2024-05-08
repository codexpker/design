package com.xpker.common.filter;

import com.alibaba.fastjson2.JSON;
import com.xpker.common.utils.JwtUtil;
import com.xpker.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //这里的X-Token是于前端对接的，如果要修改，修改前端即可
        String token = request.getHeader("X-Token");
        //写到日志中，不影响后续生产环境下的性能
        log.debug(request.getRequestURI() + "需要验证" + token);
        if(token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + " 验证通过");
                //放行
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败，禁止访问");
        //设置字符集和返回类型
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(20004, "jwt无效，请重新登录");
        response.getWriter().write(JSON.toJSONString(fail));
        //拦截
        return false;
    }
}
