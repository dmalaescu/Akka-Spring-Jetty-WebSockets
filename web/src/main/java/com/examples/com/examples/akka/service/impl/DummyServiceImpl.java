package com.examples.com.examples.akka.service.impl;

import com.examples.akka.service.DummyService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */

@Service
public class DummyServiceImpl implements DummyService{
    @Override
    public String dummyMe() {
        return "dummy service";
    }
}
