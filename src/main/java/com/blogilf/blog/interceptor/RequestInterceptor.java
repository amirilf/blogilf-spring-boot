package com.blogilf.blog.interceptor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.blogilf.blog.model.entity.Request;
import com.blogilf.blog.model.repository.RequestRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final RequestRepository requestRepository;

    RequestInterceptor(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("start_time", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
        long res_time = System.currentTimeMillis() -  (Long) request.getAttribute("start_time");
        
        Request requestObj = new Request();
        requestObj.setTime(LocalDateTime.now());
        requestObj.setDuration(res_time);
        requestObj.setUri(request.getRequestURI());
        requestObj.setMethod(request.getMethod());
        requestObj.setStatus(response.getStatus());

        requestRepository.save(requestObj);
    }
    
}