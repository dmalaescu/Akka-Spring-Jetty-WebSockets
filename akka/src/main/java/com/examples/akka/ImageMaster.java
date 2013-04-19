package com.examples.akka;

import akka.actor.UntypedActor;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */

public class ImageMaster extends UntypedActor{

    /**
     * To be implemented by concrete UntypedActor. Defines the message handler.
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String){
            getSender().tell(message.toString() + " back");
        } else {
            unhandled(message);
        }
    }
}
