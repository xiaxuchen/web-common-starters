package com.originit.response.result;


import com.originit.common.exception.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
     private Boolean success;

    public PlatformResult() {
    }

    public PlatformResult(T data) {
        this.code = ResultCode.SUCCESS.code();
        this.msg = ResultCode.SUCCESS.message();
        this.data = data;
    }

    public static <T> Result success(T data, String msg) {
        return new PlatformResult<>(ResultCode.SUCCESS.code(),msg,data,true);
    }

    public static <T> Result success(T data) {
        return new PlatformResult<T>(ResultCode.SUCCESS.code(),"success",data,true);
    }

    public static <T> Result fail (Integer code,String msg,T data) {
        return new PlatformResult<T>(code,msg,data,false);
    }

    public static <T> Result fail (Integer code,String msg) {
        return new PlatformResult<T>(code,msg,null,false);
    }

}
