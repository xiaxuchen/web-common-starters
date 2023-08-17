package com.originit.response.interceptor;


import com.originit.response.anotation.OriginResponse;
import com.originit.response.constant.Const;
import com.originit.response.property.ResponseProperty;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseResultInterceptor implements HandlerInterceptor {

    private final ResponseProperty responseProperty;

    public ResponseResultInterceptor(ResponseProperty responseProperty) {
        this.responseProperty = responseProperty;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object originHandler) {
        if(originHandler instanceof HandlerMethod)
        {
            HandlerMethod handler = (HandlerMethod) originHandler;
            // 如果有@OriginResponse注解将不拦截
            if(handler.getBeanType().isAnnotationPresent(OriginResponse.class) || handler.getMethod().isAnnotationPresent(OriginResponse.class)) {
                request.setAttribute(Const.RESPONSE_RESULT, false);
                return true;
            }
            //如果调用的Controller的方法或Controller类上使用了ResponseResult注解，
            // 则将响应类型放入请求
            boolean isJson = (handler.getBeanType().isAnnotationPresent(RestController.class) ||
                    handler.getMethod().isAnnotationPresent(ResponseBody.class) ||
                    handler.getBeanType().isAnnotationPresent(ResponseBody.class)) && !handler.getMethod().getReturnType().isAssignableFrom(ModelAndView.class);
            request.setAttribute(Const.RESPONSE_RESULT, isJson);
        } else {
            request.setAttribute(Const.RESPONSE_RESULT, false);
        }
        return true;
    }
}
