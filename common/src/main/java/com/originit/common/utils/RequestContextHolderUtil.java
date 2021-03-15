package com.originit.common.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author xxc、
 */
public class RequestContextHolderUtil {

            public static HttpServletRequest getRequest() {
                return getRequestAttributes().getRequest();
            }

            public static HttpServletResponse getResponse() {
                return getRequestAttributes().getResponse();
            }

            public static HttpSession getSession() {
                return getRequest().getSession();
            }

            public static ServletRequestAttributes getRequestAttributes() {
                return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            }

            public static ServletContext getServletContext() {
                return ContextLoader.getCurrentWebApplicationContext().getServletContext();
            }

        }