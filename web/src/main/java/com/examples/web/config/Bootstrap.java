package com.examples.web.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinRouter;
import com.examples.akka.ImageMaster;
import com.examples.akka.ImageWorker;
import com.examples.akka.service.ImageService;
import com.examples.spring.di.DependencyInjectionProps;
import com.examples.web.GoogleQuery;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Configuration
public class Bootstrap {

    @Autowired
    ApplicationContext applicationContext;


    @Bean(name = "imageService")
    public ImageService imageService(){
        return new GoogleQuery();
    }

    @Bean(name = "actorSystem")
    @Lazy
    public ActorSystem actorSystem(){
        return ActorSystem.create("Spring_Akka_Websockets");
    }

    @Bean(name = "imageMaster")
    @DependsOn("actorSystem")
    @Lazy
    public ActorRef imageMaster(){
        return actorSystem().actorOf(
                new DependencyInjectionProps(applicationContext, ImageMaster.class),"imageMaster"
        );
    }

    @Bean(name = "imageWorker")
    @DependsOn("actorSystem")
    @Lazy
    public ActorRef imageWorker(){
        return actorSystem().actorOf(
                (new DependencyInjectionProps(applicationContext, ImageWorker.class))
                        .withRouter(new RoundRobinRouter(4)),"imageWorker"
        );
    }

    static class SpringAppContext implements ApplicationContextAware{

        private static ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public ApplicationContext getApplicationContext(){
            return applicationContext;
        }
    }

    @Bean(name = "springAppContext")
    @Lazy
    public SpringAppContext springAppContext(){
        return new SpringAppContext();
    }

}
