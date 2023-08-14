package com.originit.response.success;

import com.originit.response.property.ResponseProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DefaultSuccessCodeAcquirer implements SuccessCodeAcquirer{

    @Autowired
    ResponseProperty responseProperty;

    @Override
    public Integer getSuccessCode() {
        return responseProperty.getSuccessCode();
    }
}
