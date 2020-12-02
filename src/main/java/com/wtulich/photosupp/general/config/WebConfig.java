package com.wtulich.photosupp.general.config;

import com.wtulich.photosupp.userhandling.service.api.ui.UserRestService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {UserRestService.class})
public class WebConfig implements WebMvcConfigurer {

    @Bean(name = "localeResolver")
    public SessionLocaleResolver getSessionLocaleResolver(){
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);

        return resolver;
    }

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/views/");
        resolver.setSuffix(".jsp");
        resolver.setContentType("text/html; charset=UTF-8");

        return resolver;
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource resourceBundle = new ReloadableResourceBundleMessageSource();
        resourceBundle.setBasename("classpath:messages");
        resourceBundle.setDefaultEncoding("UTF-8");

        return resourceBundle;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}