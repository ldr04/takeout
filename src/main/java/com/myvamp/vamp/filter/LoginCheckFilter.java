package com.myvamp.vamp.filter;


import com.alibaba.fastjson.JSON;
import com.myvamp.vamp.common.BaseContext;
import com.myvamp.vamp.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {


    private static final AntPathMatcher PATH_MATCHER= new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        log.info("拦截到请求路径：{}",requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //判断要访问的页面是否与登陆有关，是的话就放行
        boolean check = checkUri(requestURI, urls);
        if(check){
            log.info("本次请求{}不需要处理，已放行",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断后台是否已登陆
        if(request.getSession().getAttribute("employee")!=null){
            log.info("本次请求用户已登录，id为{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //判断移动端是否已登陆
        if(request.getSession().getAttribute("user")!=null){
            log.info("本次请求用户已登录，id为{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //未登录则返回客户端信息
        log.info("本次请求{}用户未登录，已跳转",requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
//        filterChain.doFilter(request,response);

    }


    /**
     * 检查当前的路径是否需要拦截
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean checkUri(String requestURI,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


}
