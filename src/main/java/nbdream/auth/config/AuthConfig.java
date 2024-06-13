package nbdream.auth.config;

import lombok.RequiredArgsConstructor;
import nbdream.auth.presentation.interceptor.AuthenticatedMemberResolver;
import nbdream.auth.presentation.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final AuthenticatedMemberResolver authenticatedMemberResolver;
    private final AuthenticationInterceptor authenticationInterceptor;

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberResolver);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
                .allowCredentials(true);
    }

//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/social/**", "/swagger-ui/**");
//    }
}
