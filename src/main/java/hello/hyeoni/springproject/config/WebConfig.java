package hello.hyeoni.springproject.config;

import hello.hyeoni.springproject.notification.NotificationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final NotificationInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .collect(Collectors.toList());

        Stream<String> stream = Stream.of("/js/**","/css/**","/fonts/**","/images/**","/node_modules/**");
        List<String> list = Arrays.asList(stream.toArray(String[]::new));

        staticResourcesPath.addAll(list);

        registry.addInterceptor(interceptor).excludePathPatterns(staticResourcesPath);
    }
}
