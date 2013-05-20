package com.examples.akka;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Future;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import com.examples.akka.communication.Message;
import com.examples.akka.communication.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
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

    Map<String, Integer> count = new HashMap<String, Integer>();

    @Override
    public void onReceive(final Object message) throws Exception {
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
                    if (result == null) return;
                    Message<String> imageDataMessage = new Message<String>();
                    imageDataMessage.setType(MessageType.IMAGE);
                    imageDataMessage.setData(result.toString());
                    // send image data
                    sender.tell(imageDataMessage);
                }
            });

//            response.onSuccess(new OnSuccess<Object>() {
//                /**
//                 * This method will be invoked once when/if a Future that this callback is registered on
//                 * becomes successfully completed
//                 */
//                @Override
//                public void onSuccess(Object result) {
//                     // update count
//                     updateCount((String)message);
//                     // send aggregate data;
//                     Message<Map> aggregateMessage = new Message<Map>();
//                     aggregateMessage.setType(MessageType.AGGREGATE);
//                     aggregateMessage.setData(count);
//                     sender.tell(aggregateMessage);
//                }
//            });

        } else {
            unhandled(message);
        }



    }


    private void updateCount(String imageCategory){
        if(count.get(imageCategory) == null){
            count.put(imageCategory, 1);
        } else {
            count.put(imageCategory, count.get(imageCategory) + 1);
        }
    }
}
