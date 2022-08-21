package org.originit.common.userservice.web;

import com.originit.response.anotation.ResponseResult;
import org.originit.common.userservice.callback.LoginUserCallback;
import org.originit.common.userservice.entity.ILoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xxc
 */
@RestController
@ResponseResult
@RequestMapping("/user")
public class UserController {

    @Autowired
    LoginUserCallback loginUserCallback;

    @PostMapping("/login")
    public void login(HttpServletRequest request) {
        final ILoginUser loginUser = loginUserCallback.getLoginUser(request);
    }

    @PostMapping("/register")
    public void registerUser(HttpServletRequest request) {

    }
}
