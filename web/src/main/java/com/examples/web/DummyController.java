package com.examples.web;

import com.examples.akka.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
@Controller
@RequestMapping("/controller")
public class DummyController {

    @Autowired
    ImageService dummyService;

    @RequestMapping(value = "/dummy", method = RequestMethod.GET)
    public @ResponseBody String dummyService(){
        return "ahaa!";
    }

}
