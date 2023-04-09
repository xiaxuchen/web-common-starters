package com.originit.common.param.anno;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BodyFieldArgResolver implements HandlerMethodArgumentResolver {


    public static final String APPLICAITON_JSON = "application/json";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(BodyField.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        final BodyField bodyField = methodParameter.getParameterAnnotation(BodyField.class);
        final HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final String contentType = request.getContentType();
        if (!contentType.equals(APPLICAITON_JSON)) {
            throw new IllegalArgumentException("not json request");
        }
        final String body = IoUtil.read(request.getReader());
        String path = bodyField.value();
        if (StrUtil.isBlank(path)) {
            path = methodParameter.getParameterName();
        }
        final JSON jsonParsed = JSONUtil.parse(body);
        final Object obj = jsonParsed.getByPath(path);
        if (ClassUtil.isBasicType(methodParameter.getParameterType()) || methodParameter.getParameterType().isAssignableFrom(String.class)){
            return Convert.convert(methodParameter.getParameterType(), obj);
        } else {
            if (obj instanceof JSONArray) {
                if (methodParameter.getParameterType().isAssignableFrom(List.class)) {
                    final ParameterizedType genericParameterType = (ParameterizedType) methodParameter.getGenericParameterType();
                    final Type actualTypeArgument = genericParameterType.getActualTypeArguments()[0];
                    final Object[] objects = ((JSONArray) obj).stream().toArray();
                    return Arrays.stream(objects).map(o -> (JSONObject)o).map(jsonObject -> jsonObject.toBean(actualTypeArgument)).collect(Collectors.toList());
                }
                throw new IllegalArgumentException("参数类型错误，json数据" + methodParameter.getParameterName() + "是数组，请使用List接收而不是" + methodParameter.getParameterType().getName());
            } else if (obj instanceof JSONObject) {
                return ((JSONObject) obj).toBean(methodParameter.getParameterType());
            }
            else {
                throw new IllegalArgumentException("参数类型错误，json数据是基本类型，但参数[" + methodParameter.getParameterName() + "]不为基本类型:" + methodParameter.getParameterType());
            }
        }
    }
}
