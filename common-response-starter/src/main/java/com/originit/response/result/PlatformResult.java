package com.originit.response.result;


import com.originit.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用响应
 * @author xxc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlatformResult<T> extends Result{

     private Integer code;
     private String msg;
     private T data;

    public PlatformResult() {
    }

    public PlatformResult(T data) {
        this.code = CommonConstant.CODE_SUCCESS;
        this.msg = CommonConstant.MSG_SUCCESS;
        this.data = data;
    }

    public static <T> PlatformResult<T> success(T data, String msg) {
        return new PlatformResult<>(CommonConstant.CODE_SUCCESS,msg,data);
    }

    public static <T> PlatformResult<T> success(T data) {
        return new PlatformResult<T>(CommonConstant.CODE_SUCCESS,"success",data);
    }

    public static <T> PlatformResult<T> fail (Integer code,String msg,T data) {
        return new PlatformResult<T>(code,msg,data);
    }

    public static <T> PlatformResult<T> fail (Integer code,String msg) {
        return new PlatformResult<T>(code,msg,null);
    }

}
