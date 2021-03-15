package com.originit.response.result;

import java.io.Serializable;
import java.util.Map;

/**
 * 全局响应对象
 * @author xxc、
 */

public abstract class Result implements Serializable{

    public Result() {}
    /**
     * 成功情况下的Result
     * @param data 数据
     */
    public Result(Object data){ }
}
