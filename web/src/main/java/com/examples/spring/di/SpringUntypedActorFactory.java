package com.examples.spring.di;

import akka.actor.Actor;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class SpringUntypedActorFactory implements UntypedActorFactory{
    private final DependencyInjectionFactory dependencyInjectionFactory;

    private final ApplicationContext applicationContext;

    public SpringUntypedActorFactory(Class<?> actorClass, ApplicationContext applicationContext) {
        this.dependencyInjectionFactory = new DefaultUntypedActorFactory(actorClass);
        this.applicationContext = applicationContext;
    }

    public SpringUntypedActorFactory(UntypedActorFactory customFactory, ApplicationContext applicationContext) {
        this.dependencyInjectionFactory = new SpecificUntypedActorFactory(customFactory);
        this.applicationContext = applicationContext;
    }

    private interface DependencyInjectionFactory {
        UntypedActor createAndInject();
    }


    private abstract class AbstractUntypedActorFactory implements DependencyInjectionFactory {

        @Override
        public final UntypedActor createAndInject() {
            try {
                UntypedActor untypedActor = create();

                Class<?> aClass = getActorClass();
                for (Field field : aClass.getDeclaredFields()) {

                    if (field.getAnnotation(Autowired.class) != null) {
                        boolean accessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            if (field.getAnnotation(Qualifier.class) != null){
                                Annotation annotation = field.getAnnotation(Qualifier.class);
                                field.set(untypedActor, applicationContext.getBean(field.getAnnotation(Qualifier.class).value(), field.getType()));
                            } else {
                                field.set(untypedActor, applicationContext.getBean(field.getType()));
                            }
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("Unable to create actor instance", e);
                        }
                    }
                }
                return untypedActor;

            } catch (InstantiationException e) {
                throw new IllegalStateException("Unable to create actor instance", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Unable to create actor instance", e);
            }

        }

        protected abstract Class<?> getActorClass();

        protected abstract UntypedActor create() throws InstantiationException, IllegalAccessException;

    }

    private final class SpecificUntypedActorFactory extends AbstractUntypedActorFactory {

        private final UntypedActorFactory specificFactory;
        private volatile Class<?> actorClass;

        private SpecificUntypedActorFactory(UntypedActorFactory specificFactory) {
            this.specificFactory = specificFactory;
        }

        @Override
        protected Class<?> getActorClass() {
            return actorClass;
        }

        @Override
        protected UntypedActor create() throws InstantiationException, IllegalAccessException {
            UntypedActor untypedActor = (UntypedActor) specificFactory.create();
            actorClass = untypedActor.getClass();
            return untypedActor;
        }
    }

    private final class DefaultUntypedActorFactory extends AbstractUntypedActorFactory {
        private final Class<?> actorClass;

        public DefaultUntypedActorFactory(Class<?> actorClass) {
            this.actorClass = actorClass;
        }

        @Override
        protected Class<?> getActorClass() {
            return actorClass;
        }

        @Override
        protected UntypedActor create() throws InstantiationException, IllegalAccessException {
            return (UntypedActor) actorClass.newInstance();
        }
    }

    /**
     * This method must return a different instance upon every call.
     */
    @Override
    public UntypedActor create() {
        return dependencyInjectionFactory.createAndInject();
    }
}
