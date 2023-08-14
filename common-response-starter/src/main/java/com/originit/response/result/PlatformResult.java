package com.originit.response.result;


import com.originit.common.constant.CommonConstant;
import com.originit.common.utils.SpringUtil;
import com.originit.response.success.SuccessCodeAcquirer;
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

    public PlatformResult(T data) {
        this.code = SpringUtil.getBean(SuccessCodeAcquirer.class).getSuccessCode();
        this.data = data;
    }



    public static <T> PlatformResult<T> success(T data, String msg) {
        return new PlatformResult<>(SpringUtil.getBean(SuccessCodeAcquirer.class).getSuccessCode(),msg,data);
    }

    public static <T> PlatformResult<T> success(T data) {
        return new PlatformResult<T>(SpringUtil.getBean(SuccessCodeAcquirer.class).getSuccessCode(),"success",data);
    }

    public static <T> PlatformResult<T> fail (Integer code,String msg,T data) {
        return new PlatformResult<T>(code,msg,data);
    }

    public static <T> PlatformResult<T> fail (Integer code,String msg) {
        return new PlatformResult<T>(code,msg,null);
    }

}
