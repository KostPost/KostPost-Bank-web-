//package org.web.webauthorization;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.WebContentInterceptor;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//
//        registry.addResourceHandler("/css/**")
//                .addResourceLocations("classpath:/static/css/");
//
//        registry.addResourceHandler("/js/**")
//                .addResourceLocations("classpath:/static/js/");
//
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("classpath:/static/images/");
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
//
//        // Настройка для запрета кэширования всех ответов
//        webContentInterceptor.addCacheMapping(CacheControl.noCache().cachePrivate(), "/**");
//
//        registry.addInterceptor(webContentInterceptor);
//    }
//}
//
