package com.examples;

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

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Configuration
@ComponentScan("com.example")
public class Bootstrap {
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public DummyService dummyService(){
        return new DummyServiceImpl();
    }

    @Bean(name = "actorSystem")
    public ActorSystem actorSystem(){
        return ActorSystem.create("spring-akka-websockets");
    }

    @Bean
    @DependsOn("actorSystem")
    public ActorRef imageMaster(){
        return actorSystem().actorOf(
                new DependencyInjectionProps(applicationContext, ImageMaster.class),"imageMaster"
        );
    }

    @Bean
    @DependsOn("actorSystem")
    public ActorRef imageWorker(){
        return actorSystem().actorOf(
                (new DependencyInjectionProps(applicationContext, ImageWorker.class))
                        .withRouter(new RoundRobinRouter(4)),"imageWorker"
        );
    }

}
