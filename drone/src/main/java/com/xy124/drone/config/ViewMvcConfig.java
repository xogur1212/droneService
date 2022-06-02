package com.xy124.drone.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class ViewMvcConfig extends DroneUiWebMvcConfig{


    public ViewMvcConfig(ApplicationContext applicationContext) {
        super(applicationContext);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/view/index");
        registry.addViewController("/test").setViewName("/view/test/droneMissionTest");
    }

}
