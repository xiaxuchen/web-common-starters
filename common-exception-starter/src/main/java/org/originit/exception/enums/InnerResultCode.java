package org.originit.exception.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc API 内部统一返回状态码
 * 为了不与用户的code冲突，以1111为前缀
 * @since 9/20/2018
 */
public enum InnerResultCode {

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(111110001, "参数无效"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(111140001, "系统繁忙，请稍后重试"),
    REQUEST_ERROR(111140002, "请求错误!"),
    REQUEST_METHOD_ERROR(111140003, "请求方法不支持!"),


    /* 权限错误：70001-79999 */
    PERMISSION_INVAILD(111170001,"权限异常"),
    TOKEN_NOT_FOUND(111170002,"未找到token信息"),

    INVALID_TOKEN_ERROR(111170003,"token信息异常"),
    TOKEN_EXPIRED(111170004,"token已过期"),
    /**
     * 状态异常
     *
     */
    ILLEGAL_STATE(111180001, "状态异常");

    private Integer code;

    private String message;

    InnerResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (InnerResultCode item : InnerResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (InnerResultCode item : InnerResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

    /***
     * 校验重复的code值
     */
    static void main(String[] args) {
        InnerResultCode[] apiResultCodes = InnerResultCode.values();
        List<Integer> codeList = new ArrayList<Integer>();
        for (InnerResultCode apiResultCode : apiResultCodes) {
            if (codeList.contains(apiResultCode.code)) {
                System.out.println(apiResultCode.code);
            } else {
                codeList.add(apiResultCode.code());
            }

            System.out.println(apiResultCode.code() + " " + apiResultCode.message());
        }
    }
}
