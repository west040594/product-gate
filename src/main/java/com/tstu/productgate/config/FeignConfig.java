package com.tstu.productgate.config;

import com.tstu.productgate.components.RequestContext;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class FeignConfig {

    @Value("${security.jwt.token}")
    private String jwtToken;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestContext tokenService() {
        return new RequestContext();
    }

    @Bean
    public RequestInterceptor requestInterceptor(RequestContext requestContext) {


        return requestTemplate -> requestTemplate
                .header(AUTHORIZATION, requestContext.getToken())
                .header("Service", requestContext.getRequestService());
    }
}
