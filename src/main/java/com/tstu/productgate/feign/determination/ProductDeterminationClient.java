package com.tstu.productgate.feign.determination;

import com.tstu.commons.dto.http.response.determination.PredictionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-determination", url = "${feign.services.productdetermination.url}")
public interface ProductDeterminationClient {

    @PostMapping(value = "/predict/{modelName}", consumes = "multipart/form-data" )
    ResponseEntity<PredictionResponse> predictImage(@RequestPart("file") MultipartFile file, @PathVariable("modelName") String modelName);

    @GetMapping(value = "/predict/labels/{modelName}")
    ResponseEntity<List<String>> getClassLabels(@PathVariable("modelName") String modelName);

    @GetMapping(value = "/predict/aliases")
    ResponseEntity<Set<String>> getAliases();

    @PostMapping(value = "/train/{modelName}")
    ResponseEntity<String> trainModel(@PathVariable("modelName") String modelName);
}
