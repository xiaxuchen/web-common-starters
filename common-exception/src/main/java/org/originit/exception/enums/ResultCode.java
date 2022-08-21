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
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_ERROR(20001,"用户异常"),
    USER_NOT_LOGGED_IN(20002, "用户未登录"),
    USER_LOGIN_ERROR(20003, "密码错误"),
    USER_ACCOUNT_FORBIDDEN(20004, "账号已被禁用"),
    USER_NOT_EXIST(20005, "用户不存在"),
    USER_HAS_EXISTED(20006, "用户已存在"),
    LOGIN_CREDENTIAL_EXISTED(20007, "凭证已存在"),

    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "业务错误"),
    VERIFY_CODE_INVALID(30101,"验证码已失效,请重新获取"),
    VERIFY_CODE_ERROR(30102,"验证码错误"),
    /* 聊天业务异常 33001 - 33050*/
    CHAT_ERROR(33002,"聊天异常"),
    CHAT_USER_IS_OFFLINE(33003,"用户已离线"),
    CHAT_USER_IS_ALREADY_RECEIVED(33004,"用户已被接入"),
    /* 文件存取业务异常 33051-33100 */
    FILE_ERROR(33051,"文件异常"),
    FILE_NOT_FOUND_ERROR(33052,"文件找不到"),
    FILE_CODE_NOT_EXIST(33053,"文件找不到"),
    USER_IS_NOT_AGENT(33054,"用户不是经理"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),
    SYSTEM_LOCK_FAIL(40002, "系统繁忙,请稍后重试"),

    /* 数据错误：50001-599999 */
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_INVAILD(70000,"权限异常"),
    PERMISSION_NO_ACCESS(70001, "无访问权限"),
    RESOURCE_EXISTED(70002, "资源已存在"),
    RESOURCE_NOT_EXISTED(70003, "资源不存在"),

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
