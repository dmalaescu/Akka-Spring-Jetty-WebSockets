package com.examples.akka;

import akka.actor.UntypedActor;
import com.examples.akka.service.ImageService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class ImageWorker extends UntypedActor{
    /**
     * To be implemented by concrete UntypedActor. Defines the message handler.
     */

    @Autowired
    @Qualifier("imageService")
    ImageService imageService;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String){
            String imageName = (String)message;
            byte[] bytes = imageService.downloadImage(imageName);
            // return the image encoded in base 64
            getSender().tell(Base64.encode(bytes));
        } else {
            unhandled(message);
        }
    }
}
