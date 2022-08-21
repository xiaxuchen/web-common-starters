package com.originit.logger.handler;


import com.originit.logger.annotation.Log;
import com.originit.common.utils.RequestContextHolderUtil;
import com.originit.logger.annotation.NoLog;
import com.originit.logger.enums.HeaderConstants;
import com.originit.logger.property.LogProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Aspect
public class LoggerAspect {

    private LogProperty logProperty;

    public LoggerAspect(LogProperty logProperty) {
        this.logProperty = logProperty;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 环绕通知
     * @param joinPoint 连接点
     * @return 切入点返回值
     * @throws Throwable 异常信息
     */
    @Around("within(@org.springframework.web.bind.annotation.RestController *) || @annotation(com.originit.logger.annotation.Log)")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Boolean logFlag = this.needToLog(method);
        if (!logFlag) {
            return joinPoint.proceed();
        }
        String methodName = this.getMethodName(joinPoint);
        String params = this.getParamsJson(joinPoint);
        String logResult = "";

        // 若当前无请求上下文，直接打印
        try {
            RequestContextHolderUtil.getRequest();
        }catch (Exception e) {
            long start = System.currentTimeMillis();
            logger.info("Started method 【{}】 params 【{}】", methodName, params);
            final Object proceed = joinPoint.proceed();
            if (proceed != null) {
                logResult = proceed.toString();
            }
            logger.info(" Ended method 【{}】 params 【{}】result 【{}】cost [{}] millis ", methodName, params,logResult,System.currentTimeMillis() - start);
            return proceed;
        }
        HttpServletRequest request = RequestContextHolderUtil.getRequest();
        Object requester = request.getSession().getAttribute("auth");
        String ip = getIpAddress(request);
        String callSource = request.getHeader(HeaderConstants.CALL_SOURCE);
        String appVersion = request.getHeader(HeaderConstants.APP_VERSION);
        String apiVersion = request.getHeader(HeaderConstants.API_VERSION);
        String userAgent = request.getHeader("user-agent");

        logger.info("Started request requester [{}] method [{}] params [{}] IP [{}] callSource [{}] appVersion [{}] apiVersion [{}] userAgent [{}]", requester, methodName, params, ip, callSource, appVersion, apiVersion, userAgent);
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        if(result != null)
        {
            logResult = result.toString();
        }
        logger.info("Ended request requester [{}] method [{}] params[{}] response is [{}] cost [{}] millis ",
                requester, methodName, params, logResult, System.currentTimeMillis() - start);
        return result;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        String shortMethodNameSuffix = "(..)";
        if (methodName.endsWith(shortMethodNameSuffix)) {
            methodName = methodName.substring(0, methodName.length() - shortMethodNameSuffix.length());
        }
        return methodName;
    }

    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg:args) {
            if(arg == null)
            {
                continue;
            }
            //移除敏感内容
            String paramStr;
            if (arg instanceof HttpServletResponse) {
                paramStr = HttpServletResponse.class.getSimpleName();
            } else {
                if(arg instanceof  HttpServletRequest)
                {
                    paramStr = HttpServletRequest.class.getSimpleName();
                } else if (arg instanceof MultipartFile)
                {
                    long size = ((MultipartFile) arg).getSize();
                    paramStr = MultipartFile.class.getSimpleName() + ":" + ((MultipartFile) arg).getName() + "size:" + size;
                } else {
                    paramStr = arg.toString();
                }
            }
            sb.append(paramStr).append(",");
        }
        if(sb.length() != 0) {
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return sb.toString();
        }
    }

    /**
     * 判断是否需要记录日志
     */
    private Boolean needToLog(Method method)  {
        if (logProperty.isDebug()) {
            return true;
        }
        if(method.getAnnotation(NoLog.class) != null || method.getDeclaringClass().getAnnotation(NoLog.class) != null) {
            return false;
        }
        //如果方法为get请求则根据配置是否打印，除非方法、类上有Log注解
        return (logProperty.isLogGet() && method.getAnnotation(GetMapping.class) != null) || method.getAnnotation(Log.class) != null
                || method.getDeclaringClass().getAnnotation(Log.class) != null;
    }

    /**
     * 获取真实ip地址，避免获取代理ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

//    /**
//     * 删除参数中的敏感内容
//     * @param obj 参数对象
//     * @return 去除敏感内容后的参数对象
//     */
//    private fun deleteSensitiveContent(obj: Any?): String {
//        var jsonObject = JSONObject()
//        if (obj == null || obj is Exception) {
//            return jsonObject.toJSONString()
//        }
//
//        try {
//            val param = GsonUtil.toJson(obj)
//            jsonObject = JSONObject.parseObject(param)
//            val sensitiveFieldList = this.sensitiveFieldList
//            for (sensitiveField in sensitiveFieldList) {
//                if (jsonObject.containsKey(sensitiveField)) {
//                    jsonObject.put(sensitiveField, "******")
//                }
//            }
//        } catch (e: ClassCastException) {
//            return obj.toString()
//        }
//
//        return obj.toString()
//    }
}
