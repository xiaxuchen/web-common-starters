package org.originit.exception.enums;

import com.originit.common.constant.CommonConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumaer
 * @desc API 统一返回状态码
 * @since 8/31/2017 3:00 PM
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(CommonConstant.CODE_SUCCESS, CommonConstant.MSG_SUCCESS),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 权限错误：70001-79999 */
    PERMISSION_INVAILD(70001,"权限异常"),
    TOKEN_NOT_FOUND(70002,"未找到token信息"),

    INVALID_TOKEN_ERROR(70003,"token信息异常"),
    TOKEN_EXPIRED(70004,"token已过期"),
    /**
     * 状态异常
     *
     */
    ILLEGAL_STATE(80001, "状态异常");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
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
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
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
        ResultCode[] apiResultCodes = ResultCode.values();
        List<Integer> codeList = new ArrayList<Integer>();
        for (ResultCode apiResultCode : apiResultCodes) {
            if (codeList.contains(apiResultCode.code)) {
                System.out.println(apiResultCode.code);
            } else {
                codeList.add(apiResultCode.code());
            }

            System.out.println(apiResultCode.code() + " " + apiResultCode.message());
        }
    }
}
