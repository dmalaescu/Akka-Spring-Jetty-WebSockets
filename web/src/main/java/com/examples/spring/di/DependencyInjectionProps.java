package com.examples.spring.di;

import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class DependencyInjectionProps extends Props{

    public DependencyInjectionProps(ApplicationContext applicationContext, Class<?> actorClass) {
        super(new SpringUntypedActorFactory(actorClass, applicationContext));
    }

    public DependencyInjectionProps(ApplicationContext applicationContext, UntypedActorFactory factory) {
        super(new SpringUntypedActorFactory(factory, applicationContext));
    }


}
