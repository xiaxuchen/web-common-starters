package com.originit.response.handler;


import com.originit.response.constant.Const;
import com.originit.common.utils.RequestContextHolderUtil;
import com.originit.response.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Class type = (Class) RequestContextHolderUtil.getRequest().getAttribute(Const.RESPONSE_RESULT);
        return type != null && !BeanUtils.isSimpleProperty(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Class type = (Class) RequestContextHolderUtil.getRequest().getAttribute(Const.RESPONSE_RESULT);
        // 如果是Result类型的就不包装了
        if(body instanceof Result) {
            return body;
        }
        //如果是通用响应类则进行创建返回
        try {
            return type.getConstructor(Object.class).newInstance(body);
        } catch (Exception e) {
            throw new IllegalStateException("Result的子类必须包含一个只有一个Object类型的构造器用于构造成功请求");
        }
    }
}
