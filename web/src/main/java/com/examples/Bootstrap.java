package com.examples;

import com.examples.akka.service.DummyService;
import com.examples.com.examples.akka.service.impl.DummyServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Configuration
@ComponentScan("com.example")
public class Bootstrap {

    @Bean
    public DummyService dummyService(){
        return new DummyServiceImpl();
    }
}
