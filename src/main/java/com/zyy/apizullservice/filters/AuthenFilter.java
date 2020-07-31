package com.zyy.apizullservice.filters;

import com.netflix.client.http.HttpResponse;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AuthenFilter extends ZuulFilter {
    /**
     * filter 类型 pre route post error
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 执行顺序，值越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return -100;
    }

    /**
     * 返回 true 执行 run 方法，返回 false 不执行 run 方法
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 可以根据请求路径判断哪些需要进行 run 校验，哪些不要
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // RequestContext 继承了 ConcurrentHashMap 是个 kv 数据结构，存储了一个请求的所有信息
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取当前请求
        HttpServletRequest request = currentContext.getRequest();
        String parameter = request.getParameter("login-toke");
        if (StringUtils.isEmpty(parameter)){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            currentContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
            currentContext.setResponseBody("{'403','请登录'}");
            currentContext.getResponse().setCharacterEncoding("UTF-8");
        }
        return null;
    }
}
