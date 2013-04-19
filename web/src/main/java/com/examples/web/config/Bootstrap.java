package com.examples.web.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinRouter;
import com.examples.akka.ImageMaster;
import com.examples.akka.ImageWorker;
import com.examples.akka.service.DummyService;
import com.examples.com.examples.akka.service.impl.DummyServiceImpl;
import com.examples.spring.di.DependencyInjectionProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Configuration
@ComponentScan(basePackageClasses = {DummyServiceImpl.class})
public class Bootstrap {

    public Bootstrap(){
    }

    @Autowired
    ApplicationContext applicationContext;

    @Bean(name = "dummyService")
    public DummyService dummyService(){
        return new DummyServiceImpl();
    }

    @Bean(name = "actorSystem")
    public ActorSystem actorSystem(){
        return ActorSystem.create("Spring_Akka_Websockets");
    }

    @Bean(name = "imageMaster")
    @DependsOn("actorSystem")
    public ActorRef imageMaster(){
        return actorSystem().actorOf(
                new DependencyInjectionProps(applicationContext, ImageMaster.class),"imageMaster"
        );
    }

    @Bean(name = "imageWorker")
    @DependsOn("actorSystem")
    public ActorRef imageWorker(){
        return actorSystem().actorOf(
                (new DependencyInjectionProps(applicationContext, ImageWorker.class))
                        .withRouter(new RoundRobinRouter(4)),"imageWorker"
        );
    }

}
