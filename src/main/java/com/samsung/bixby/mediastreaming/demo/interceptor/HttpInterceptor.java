package com.samsung.bixby.mediastreaming.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(HttpInterceptor.class.getName());

    public static HttpInterceptor HttpInterceptor() {
        return new HttpInterceptor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("=== Before Method ===");
        String contentType = request.getContentType();
        String requestId = request.getHeader("requestId");
        logger.info("contentType : " + contentType);
        logger.info("requestId : " + requestId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("=== Method Executed ===");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("=== Method Completed ===");
    }
}
