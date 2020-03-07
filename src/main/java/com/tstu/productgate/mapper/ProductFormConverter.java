package com.tstu.productgate.mapper;

import com.tstu.commons.dto.http.request.productgate.forms.NewProductForm;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFormConverter extends PropertyMap<NewProductForm, ProductDataRequest> {

    public Converter<String, List<String>> reviewSystemLinksConverter() {
        return context -> Arrays.asList(context.getSource().split(",")).stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    protected void configure() {
        using(reviewSystemLinksConverter()).map(source.getReviewSystemLinks()).setReviewSystemLinks(null);
    }
}
