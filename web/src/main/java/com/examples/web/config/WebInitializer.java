package com.examples.web.config;

import com.examples.web.DummyWebSocketServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        // add servlet for WebSockets
        ServletRegistration.Dynamic wsRegistration = servletContext.addServlet("WebSocket", DummyWebSocketServlet.class);
//        wsRegistration.setLoadOnStartup(1);
        wsRegistration.addMapping("/ws/*");

    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {Bootstrap.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/mvc/*"};
    }
}
