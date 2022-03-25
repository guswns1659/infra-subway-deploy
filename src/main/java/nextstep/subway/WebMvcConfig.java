package nextstep.subway;

import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public static final String PREFIX_STATIC_RESOURCES = "/resources";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + "/**")
            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.noCache())
            .setCacheControl(CacheControl.maxAge(60 * 60 * 24 * 365, TimeUnit.SECONDS).cachePrivate());
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Filter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        registrationBean.setFilter(shallowEtagHeaderFilter);
        registrationBean.addUrlPatterns(PREFIX_STATIC_RESOURCES + "/*");
        return registrationBean;
    }
}
