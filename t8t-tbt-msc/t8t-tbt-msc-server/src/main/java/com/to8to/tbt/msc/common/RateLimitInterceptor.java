package com.to8to.tbt.msc.common;

import com.google.common.util.concurrent.RateLimiter;
import com.to8to.sc.compatible.RPCException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author pajero.quan
 */
@Slf4j
@Component("rateLimitInterceptor")
public class RateLimitInterceptor implements HandlerInterceptor {

    @Value("${interface.all.qps}")
    private Integer qps;

    /**
     * 全局限流器(限制QPS为200)
     */
    private ConcurrentMap<String, RateLimiter> rateLimiterMap;

    @PostConstruct
    public void init() {
        rateLimiterMap = new ConcurrentHashMap<>(10);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        RateLimiter rateLimiter = rateLimiterMap.get(uri);
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(qps);
            rateLimiterMap.putIfAbsent(uri, rateLimiter);
        }
        if (!rateLimiter.tryAcquire()) {
            log.warn("限流中！uri=" + uri);
            throw new RPCException(MyExceptionStatus.RATE_LIMIT_REACHED);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
