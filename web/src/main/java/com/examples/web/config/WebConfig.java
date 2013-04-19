package com.examples.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.examples.web"})
public class WebConfig extends WebMvcConfigurerAdapter{

}
