package im.zhaojun.common.config;

import im.zhaojun.common.interceptor.LogMDCInterceptor;
import im.zhaojun.common.interceptor.RequestLogHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RequestLogHandlerInterceptor logHandlerInterceptor;

    @Resource
    private LogMDCInterceptor shiroMDCInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shiroMDCInterceptor)
                .excludePathPatterns(Arrays.asList("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/error"));

        registry.addInterceptor(logHandlerInterceptor)
                .excludePathPatterns(Arrays.asList("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/error"));
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//文件磁盘图片url 映射
//配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler("/advertising/preview/**")
                .addResourceLocations("file:C:\\Users\\Administrator\\Desktop\\apache-tomcat-7.0.94-8082\\webapps\\");

//        默认访问static文件
       // registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

}