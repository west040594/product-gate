package com.tstu.productgate.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "feign")
public class FeignProperties {

    private Services services;

    @Data
    public static class Services {
        private Service authentication;
        private Service productinfo;
        private Service productdetermination;
    }

    @Data
    public static class Service {
        private String name;
        private String url;
    }
}
