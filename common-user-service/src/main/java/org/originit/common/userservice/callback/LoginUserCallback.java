package org.originit.common.userservice.callback;

import org.originit.common.userservice.entity.ILoginUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户的回调
 */
public interface LoginUserCallback {

    ILoginUser getLoginUser(HttpServletRequest request);
}
