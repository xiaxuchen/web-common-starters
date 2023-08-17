package com.originit.response.result;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.originit.response.constant.Const;
import com.originit.response.property.ResponseProperty;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class DefaultResultGenerator implements ResultGenerator{

    @Resource
    ResponseProperty property;

    @Override
    public Object success(Object data, String msg) {
        //如果是通用响应类则进行创建返回
        ResultMap<String, Object> map = new ResultMap<>();
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_CODE,Const.FIELD_CODE), property.getSuccessCode());
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_DATA,Const.FIELD_DATA), data);
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_MSG,Const.FIELD_MSG), msg);
        return map;
    }

    @Override
    public Object fail(Integer code, String msg, Object data) {
        //如果是通用响应类则进行创建返回
        ResultMap<String, Object> map = new ResultMap<>();
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_CODE,Const.FIELD_CODE), code);
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_DATA,Const.FIELD_DATA), data);
        map.put(property.getFieldNames().getOrDefault(Const.FIELD_MSG,Const.FIELD_MSG), msg);
        return map;
    }

}
