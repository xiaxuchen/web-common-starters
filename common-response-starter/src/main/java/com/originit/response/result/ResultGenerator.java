package com.originit.response.result;

public interface ResultGenerator {

    default Object success(Object data) {
        return success(data, "");
    }

    Object success(Object data, String msg);

    Object fail(Integer code, String msg, Object data);

    default Object fail(Integer code, String msg) {
        return fail(code, msg, null);
    }

}
