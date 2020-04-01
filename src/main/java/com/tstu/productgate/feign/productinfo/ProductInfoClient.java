package com.tstu.productgate.feign.productinfo;

import com.tstu.commons.dto.http.request.productinfo.CategoryDataRequest;
import com.tstu.commons.dto.http.request.productinfo.ProductDataRequest;
import com.tstu.commons.dto.http.response.productinfo.CategoryResponse;
import com.tstu.commons.dto.http.response.productinfo.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-info",  url = "${feign.services.productinfo.url}")
public interface ProductInfoClient {

    @PostMapping("/products/predict")
    ResponseEntity<List<ProductResponse>> getAllProductsByPredict(@RequestBody List<String> productNames);

    @GetMapping("/products/name/{productName}")
    ResponseEntity<ProductResponse> getProductByPredict(@PathVariable("productName") String productName);

    @GetMapping("/products")
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @GetMapping("/products/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id);

    @GetMapping("/products/category/{categoryName}")
    ResponseEntity<List<ProductResponse> > getProductsByCategoryName(@PathVariable("categoryName") String categoryName);

    @GetMapping("/products/category/alias/{categoryAlias}")
    ResponseEntity<List<ProductResponse> > getProductsByCategoryAlias(@PathVariable("categoryAlias") String categoryAlias);

    @PostMapping("/products/create")
    ResponseEntity<ProductResponse> createNewProduct(@RequestBody ProductDataRequest productDataRequest);

    @PostMapping("/products/parse")
    ResponseEntity<String> sendDomParserRequest(@RequestBody Long id);

    @DeleteMapping("/products/{id}")
    ResponseEntity<String> deleteProductById(@PathVariable("id") Long id);

    @GetMapping("/categories")
    ResponseEntity<List<CategoryResponse>> getAllCategories();

    @GetMapping("/categories/{id}")
    ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id);

    @GetMapping("/categories/name/{name}")
    ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name);

    @PostMapping("/categories/create")
    ResponseEntity<CategoryResponse> createNewCategory(@RequestBody CategoryDataRequest productDataRequest);

    @DeleteMapping("/categories/{id}")
    ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long id);
}
