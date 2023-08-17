package com.originit.response.handler;


import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.originit.common.utils.RequestContextHolderUtil;
import com.originit.response.constant.Const;
import com.originit.response.property.ResponseProperty;
import com.originit.response.result.ResultGenerator;
import com.originit.response.result.ResultMap;
import com.originit.response.result.SimpleData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice {

    @Resource
    ResultGenerator resultGenerator;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Boolean need = (Boolean) RequestContextHolderUtil.getRequest().getAttribute(Const.RESPONSE_RESULT);
        return need != null && need;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //如果是通用响应类则进行创建返回
        Object data = body;
        if (data instanceof ResultMap) {
            return data;
        }
        if (returnType.getParameterType() == SimpleData.class) {
            data = ((SimpleData) body).getData();
        }
        return resultGenerator.success(data);
    }
}
