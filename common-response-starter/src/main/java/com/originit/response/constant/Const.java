package com.originit.response.constant;
public interface Const {
    String RESPONSE_RESULT = "response-result";

    /**
     * 请求头中如果api-style不为空时不封装结果
     */
    String HEADER_API_STYLE = "api-style";

    /**
     * 统一响应字段
     */
    String FIELD_CODE = "code";

    String FIELD_DATA = "data";

    String FIELD_MSG = "msg";
}
