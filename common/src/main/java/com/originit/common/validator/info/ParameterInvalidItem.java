package com.originit.common.validator.info;

public class ParameterInvalidItem {
    private String fieldName;
    private String message;

    public ParameterInvalidItem() {
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
