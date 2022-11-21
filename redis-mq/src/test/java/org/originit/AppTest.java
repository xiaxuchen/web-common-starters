package org.originit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

/**
 * Unit test for simple App.
 */
@SpringBootApplication
@ActiveProfiles("test")
public class AppTest 
{
    public static void main(String[] args) {
        SpringApplication.run(AppTest.class,args);
    }
}
