package com.tstu.productgate.config;

import com.tstu.productgate.mapper.ProductFormConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new ProductFormConverter());
        return modelMapper;
    }
}
