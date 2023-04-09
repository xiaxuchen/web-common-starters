package org.originit.demo;

import com.originit.common.param.anno.BodyField;
import org.originit.demo.entity.Result;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class,args);
    }

    @RequestMapping("/testList")
    public Result hello(@BodyField List<Result> results) {
        return results.get(0);
    }

    @RequestMapping("/testBasic")
    public Result hello(@BodyField Long data) {
        final Result result = new Result();
        result.setData(data);
        return result;
    }

    @RequestMapping("/testString")
    public Result hello(@BodyField String data) {
        final Result result = new Result();
        result.setData(data);
        return result;
    }


    @RequestMapping("/testObj")
    public Result hello(@BodyField Result result) {
        return result;
    }
}
