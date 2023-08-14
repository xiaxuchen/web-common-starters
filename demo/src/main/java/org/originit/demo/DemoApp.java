package org.originit.demo;

import com.originit.logger.annotation.Log;
import com.originit.response.anotation.ResponseResult;
import com.originit.response.result.SimpleData;
import org.originit.demo.exception.UserNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;

@SpringBootApplication
@RestController
@ResponseResult
@Log
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class,args);
    }

    @RequestMapping("/hello")
    public SimpleData<String> sayHello(@RequestParam Boolean error) {
        if (error) {
            throw new UserNotFoundException( "用户找不到了", 10001);
        }
        return SimpleData.of("hahah");
    }

}
