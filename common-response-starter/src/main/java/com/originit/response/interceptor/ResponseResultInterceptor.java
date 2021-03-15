package com.originit.response.interceptor;


import com.originit.response.anotation.OriginResponse;
import com.originit.response.anotation.ResponseResult;
import com.originit.response.constant.Const;
import com.originit.response.property.ResponseProperty;
import com.originit.response.result.PlatformResult;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseResultInterceptor implements HandlerInterceptor {

    private ResponseProperty responseProperty;

    public ResponseResultInterceptor(ResponseProperty responseProperty) {
        this.responseProperty = responseProperty;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object originHandler) {
        if(originHandler instanceof HandlerMethod)
        {
            HandlerMethod handler = (HandlerMethod) originHandler;
            // 如果有@OriginResponse注解将不拦截
            if(handler.getMethod().isAnnotationPresent(OriginResponse.class)) {
                return true;
            }
            //如果调用的Controller的方法或Controller类上使用了ResponseResult注解，
            // 则将响应类型放入请求
            boolean isJson = (handler.getBeanType().isAnnotationPresent(RestController.class) ||
                    handler.getMethod().isAnnotationPresent(ResponseBody.class) ||
                    handler.getBeanType().isAnnotationPresent(ResponseBody.class)) && !handler.getMethod().getReturnType().isAssignableFrom(ModelAndView.class);
            boolean isResult = handler.getBeanType().isAnnotationPresent(ResponseResult.class)
                    || handler.getMethod().isAnnotationPresent(ResponseResult.class);
            if(isJson && isResult)
            {
                ResponseResult type = handler.getBeanType().getAnnotation(ResponseResult.class);
                if(type == null) {
                    type = handler.getMethod().getAnnotation(ResponseResult.class);
                }
                if (type == null) {
                    // 默认用这个
                    request.setAttribute(Const.RESPONSE_RESULT, PlatformResult.class);
                } else {
                    request.setAttribute(Const.RESPONSE_RESULT,type.value());
                }
            }
        }
        return true;
    }
}
