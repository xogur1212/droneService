package com.xy124.drone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public abstract class DroneUiWebMvcConfig implements WebMvcConfigurer {


    /**
     * @EnableWebMvc를 선언하면 WebMvcConfigurationSupport에서 구성한 스프링 MVC 구성을 불러온다.
     * @Configuration 과 @EnableWebMvc를 함께 선언한 클래스가 WebMvcConfigurer 인터페이스 구현
     * @EnableWebMvc 없이 스프링 MVC 구성을 변경하는 방법
     * public class WebMvcConfig extends WebMvcConfigurationSupport
     */
    private final ApplicationContext applicationContext;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonMessageConverter());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonMessageConverter());
    }

    private MappingJackson2HttpMessageConverter jsonMessageConverter() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharcterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public ViewResolver viewResolver() throws Exception {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine(templateResolver()));

        return thymeleafViewResolver;

    }

    public ITemplateResolver templateResolver() throws Exception {
        final SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setApplicationContext(applicationContext);
        springResourceTemplateResolver.setPrefix("classpath:/templates/");
        springResourceTemplateResolver.setSuffix(".html");
        springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
        springResourceTemplateResolver.setCacheable(false); //캐시 사용 안함(사용하면 html 수정시 서버 재기동 필요)

        return springResourceTemplateResolver;
    }

    public SpringTemplateEngine templateEngine(ITemplateResolver TemplateResolver) throws Exception {
        final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(TemplateResolver);
        springTemplateEngine.addDialect(new LayoutDialect());
        return springTemplateEngine;
    }

    @Bean
    public ViewResolver javascriptViewResolver() throws Exception {
        final ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine(javascriptTemplateResolver()));
        thymeleafViewResolver.setOrder(2);
        thymeleafViewResolver.setContentType("application/javascript");
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setViewNames(new String[]{"**/*.js"});


        return thymeleafViewResolver;
    }

    public ITemplateResolver javascriptTemplateResolver() {
        final SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setApplicationContext(applicationContext);
        springResourceTemplateResolver.setPrefix("classpath:/static");
        springResourceTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        springResourceTemplateResolver.setCharacterEncoding("UTF-8");
        springResourceTemplateResolver.setCacheable(false);

        return springResourceTemplateResolver;
    }

    //TODO 수정필요
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resources/**", "/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.setOrder(1);

        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
    }
}
