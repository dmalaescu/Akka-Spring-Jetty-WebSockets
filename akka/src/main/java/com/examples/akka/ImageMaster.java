package com.examples.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Future;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */

public class ImageMaster extends UntypedActor{

    /**
     * To be implemented by concrete UntypedActor. Defines the message handler.
     */
    @Autowired
    @Qualifier("imageWorker")
    ActorRef imageWorker;

    @Override
    public void onReceive(Object message) throws Exception {
//        imageWorker = applicationContext.getBean("imageWorker", ActorRef.class);
        if (message instanceof String){
            final Future<Object> response = Patterns.ask(imageWorker, message, new Timeout(Duration.create(10, TimeUnit.SECONDS)));
            final ActorRef sender = getSender();
            response.onSuccess(new OnSuccess<Object>() {
                /**
                 * This method will be invoked once when/if a Future that this callback is registered on
                 * becomes successfully completed
                 */
                @Override
                public void onSuccess(Object result) {
                    sender.tell(result);
                }
            });

        } else {
            unhandled(message);
        }
    }
}
