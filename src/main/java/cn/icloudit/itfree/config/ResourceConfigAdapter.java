package cn.icloudit.itfree.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;



@Configuration
public class ResourceConfigAdapter extends WebMvcConfigurationSupport {

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Autowired
    private LoginInterceptor loginInterceptor;

    //配置静态资源文件的路径
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 文件磁盘url映射
        // 配置server虚拟路径，handler为前台访问的目录
        // locations为file相对应的本地路径
        // 项目静态文件
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        // 项目图片上传
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则，/**表示拦截所有请求
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/checkCode","/admin/**","/home/**","/page/home/**","/","/image/**","/error","/favicon.ico","/comment/add");
        super.addInterceptors(registry);
    }



}